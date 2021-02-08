package com.avalos.sicredi.controller;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
import com.avalos.sicredi.model.Voto;
import com.avalos.sicredi.service.PautaService;
import com.avalos.sicredi.service.VotoService;

@RestController
@RequestMapping("/pautas")
public class PautaController {

	@Autowired
	private PautaService pautaService;

	@Autowired
	private VotoService votoService;

	@GetMapping({ "/v1", "/v2" })
	@Cacheable(value = "listaDePautas")
	public Page<PautaDto> listar(
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable pageable) {
		return PautaDto.converter(pautaService.findAllWithPagination(pageable));
	}

	@PostMapping({ "/v1", "/v2" })
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> cadastrar(@RequestBody @Valid PautaForm pautaForm,
			UriComponentsBuilder uriBuilder) {
		Pauta pauta = pautaService.save(pautaForm.converterToModel());

		URI uri = uriBuilder.path("/pautas/v2/{id}").buildAndExpand(pauta.getId()).toUri();
		return ResponseEntity.created(uri).body(new PautaDto(pauta));
	}

	@GetMapping("/v2/{id}")
	public ResponseEntity<PautaDto> detalhar(@PathVariable Long id) {
		return ResponseEntity.ok(new PautaDto(pautaService.findById(id)));
	}

	@PutMapping("/v2/{id}")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> atualizar(@PathVariable Long id, @RequestBody @Valid PautaForm pautaForm) {
		return ResponseEntity.ok(new PautaDto(pautaForm.atualizarModel(pautaService.findById(id))));
	}

	@DeleteMapping("/v2/{id}")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		pautaService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/v2/{id}/abrirSessao")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> abrirSessao(@PathVariable Long id,
			@RequestParam(value = "tempo", defaultValue = "1", required = false) Long tempo) {
		return ResponseEntity.ok(new PautaDto(pautaService.abrirSessao(id, tempo)));
	}

	@PostMapping("/v2/{id}/votar")
	@Transactional
	public ResponseEntity<VotoDto> votar(@PathVariable Long id, @RequestBody @Valid VotoForm votoForm) {
		Voto voto = votoService.save(id, votoForm.getUsuarioId(), votoForm.getTipo());

		return ResponseEntity.ok(new VotoDto(voto));
	}

}
