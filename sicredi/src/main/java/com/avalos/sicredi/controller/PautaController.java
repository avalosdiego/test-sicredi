package com.avalos.sicredi.controller;

import java.net.URI;
import java.time.LocalDateTime;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.avalos.sicredi.dto.PautaDto;
import com.avalos.sicredi.dto.VotoDto;
import com.avalos.sicredi.form.PautaForm;
import com.avalos.sicredi.form.VotoForm;
import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.StatusPauta;
import com.avalos.sicredi.repository.PautaRepository;
import com.avalos.sicredi.repository.VotoRepository;
import com.avalos.sicredi.service.ValidationService;

@RestController
@RequestMapping("/pautas")
public class PautaController {

	private static final String PAUTA_NAO_ENCONTRADA = "Pauta não encontrada ID: ";

	@Autowired
	private PautaRepository pautaRepository;

	@Autowired
	private VotoRepository votoRepository;
	
	@Autowired
	private ValidationService validationService;

	@GetMapping({ "/v1", "/v2" })
	@Cacheable(value = "listaDePautas")
	public Page<PautaDto> listar(@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		return PautaDto.converter(pautaRepository.findAll(paginacao));
	}

	@PostMapping({ "/v1", "/v2" })
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> cadastrar(@RequestBody @Valid PautaForm pautaForm, UriComponentsBuilder uriBuilder) {
		Pauta pauta = pautaRepository.save(pautaForm.converterToModel());

		URI uri = uriBuilder.path("/pautas/v2/{id}").buildAndExpand(pauta.getId()).toUri();
		return ResponseEntity.created(uri).body(new PautaDto(pauta));
	}

	@GetMapping("/v2/{id}")
	public ResponseEntity<PautaDto> detalhar(@PathVariable Long id) {
		Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PAUTA_NAO_ENCONTRADA + id));

		return ResponseEntity.ok(new PautaDto(pauta));
	}

	@PutMapping("/v2/{id}")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> atualizar(@PathVariable Long id, @RequestBody @Valid PautaForm pautaForm) {
		Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PAUTA_NAO_ENCONTRADA + id));

		return ResponseEntity.ok(new PautaDto(pautaForm.atualizarModel(pauta)));
	}

	@DeleteMapping("/v2/{id}")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PAUTA_NAO_ENCONTRADA + id));
		
		pautaRepository.deleteById(pauta.getId());

		return ResponseEntity.ok().build();
	}

	@PutMapping("/v2/{id}/abrirSessao")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> abrirSessao(@PathVariable Long id, @RequestParam(value = "tempo", defaultValue = "1", required = false) Long tempo) {
		Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PAUTA_NAO_ENCONTRADA + id));

		// TODO[] uma camada para converter o objeto, mas não achei necessário para simplificar o projeto
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime fechamento = now.plusMinutes(tempo);

		pauta.setDtAbertura(now);
		pauta.setDtFechamento(fechamento);
		pauta.setStatus(StatusPauta.OPENED);

		return ResponseEntity.ok(new PautaDto(pauta));
	}

	@PostMapping("/v2/{id}/votar")
	@Transactional
	public ResponseEntity<VotoDto> votar(@PathVariable Long id, @RequestBody @Valid VotoForm votoForm) {
		Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PAUTA_NAO_ENCONTRADA + id));

		validationService.validaSessao(pauta);
		validationService.validaUsuarioVotou(pauta, votoForm.getUsuarioId());
		validationService.validaUsuario(votoForm.getUsuarioId());

		return ResponseEntity.ok(new VotoDto(votoRepository.save(votoForm.converterToModel(pauta))));
	}

}
