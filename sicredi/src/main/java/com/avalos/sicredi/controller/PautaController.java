package com.avalos.sicredi.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.avalos.sicredi.config.validation.RetornoDto;
import com.avalos.sicredi.dto.PautaDto;
import com.avalos.sicredi.dto.StatusUsuario;
import com.avalos.sicredi.dto.UsuarioDto;
import com.avalos.sicredi.form.PautaForm;
import com.avalos.sicredi.form.VotoForm;
import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.StatusPauta;
import com.avalos.sicredi.model.Voto;
import com.avalos.sicredi.repository.PautaRepository;
import com.avalos.sicredi.repository.VotoRepository;

@RestController
@RequestMapping("/pautas")
public class PautaController {
	
	private static final Logger log = LoggerFactory.getLogger(PautaController.class);
	
	private static final String PAUTA_NAO_ENCONTRADA = "Pauta não encontrada"; 

	@Autowired
	private PautaRepository pautaRepository;

	@Autowired
	private VotoRepository votoRepository;

	@GetMapping({"/v1", "/v2"})
	@Cacheable(value = "listaDePautas")
	public Page<PautaDto> listar(@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {
		Page<Pauta> pautas = pautaRepository.findAll(paginacao);
		return PautaDto.converter(pautas);
	}

	@PostMapping({"/v1", "/v2"})
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> cadastrar(@RequestBody @Valid PautaForm form, UriComponentsBuilder uriBuilder) {
		Pauta pauta = form.converter();
		pautaRepository.save(pauta);
		log.info("Pauta salva");

		URI uri = uriBuilder.path("/pautas/v2/{id}").buildAndExpand(pauta.getId()).toUri();
		return ResponseEntity.created(uri).body(new PautaDto(pauta));
	}

	@GetMapping("/v2/{id}")
	public ResponseEntity<PautaDto> detalhar(@PathVariable Long id) {
		Optional<Pauta> pauta = pautaRepository.findById(id);

		if (pauta.isPresent()) {
			log.info("Pauta detalhada");
			return ResponseEntity.ok(new PautaDto(pauta.get()));
		}

		log.info(PAUTA_NAO_ENCONTRADA);
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/v2/{id}")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> atualizar(@PathVariable Long id, @RequestBody @Valid PautaForm form) {
		Optional<Pauta> optional = pautaRepository.findById(id);

		if (optional.isPresent()) {
			Pauta pauta = form.atualizar(id, pautaRepository);
			log.info("Pauta atualizada");

			return ResponseEntity.ok(new PautaDto(pauta));
		}

		log.info(PAUTA_NAO_ENCONTRADA);
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/v2/{id}")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Pauta> pauta = pautaRepository.findById(id);

		if (pauta.isPresent()) {
			pautaRepository.deleteById(id);
			log.info("Pauta deletada");
			return ResponseEntity.ok().build();
		}

		log.info(PAUTA_NAO_ENCONTRADA);
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/v2/{id}/abrirSessao")
	@Transactional
	@CacheEvict(value = "listaDePautas", allEntries = true)
	public ResponseEntity<PautaDto> abrirSessao(@PathVariable Long id, @RequestParam(value = "tempo", defaultValue = "1", required = false) Long tempo) {
		Optional<Pauta> optional = pautaRepository.findById(id);

		if (optional.isPresent()) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime fechamento = now.plusMinutes(tempo);

			optional.get().setDtAbertura(now);
			optional.get().setDtFechamento(fechamento);
			optional.get().setStatus(StatusPauta.OPENED);

			log.info("Sessão de votação aberta");
			return ResponseEntity.ok(new PautaDto(optional.get()));
		}

		log.info(PAUTA_NAO_ENCONTRADA);
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/v2/{id}/votar")
	@Transactional
	public ResponseEntity<RetornoDto> votar(@PathVariable Long id, @RequestBody @Valid VotoForm form) {
		// valida usuário
		try {
			log.info("Conectando com heroku para validar usuário");
			
			RestTemplate restTemplate = new RestTemplate();
			String uri = "https://user-info.herokuapp.com/users/" + form.getUsuarioId();

			Map<String, String> params = new HashMap<>();
			params.put("id", form.getUsuarioId());
			UsuarioDto usuarioDto = restTemplate.getForObject(uri, UsuarioDto.class);

			if (usuarioDto != null && usuarioDto.getStatus() != null && usuarioDto.getStatus().equals(StatusUsuario.UNABLE_TO_VOTE)) {
				log.info("Usuário não está apto para votar");
				return ResponseEntity.ok(new RetornoDto("Usuário não está apto para votar", true));
			}
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.info("Usuário inexistente!");
				return ResponseEntity.ok(new RetornoDto("Usuário inexistente", true));
			}
		} catch (Exception e) {
			log.error("Erro ao chamar serviço usuário! " + e.getCause());
			return ResponseEntity.ok(new RetornoDto("Erro ao chamar serviço usuário " + e.getCause(), true));
		}

		// verificar se pauta existe
		Optional<Pauta> pauta = pautaRepository.findById(id);

		if (!pauta.isPresent()) {
			log.info(PAUTA_NAO_ENCONTRADA);
			return ResponseEntity.ok(new RetornoDto("Pauta inexistente", true));
		}

		// verificar se pauta está aberta
		if (pauta.get().getDtFechamento() == null ) {
			return ResponseEntity.ok(new RetornoDto("Sessão de votação ainda não foi aberta", true));
		}

		if (pauta.get().getDtFechamento().isBefore(LocalDateTime.now())) {
			log.info("Pauta fechada");
			return ResponseEntity.ok(new RetornoDto("Pauta fechada", true));
		}

		// verificar se usuário já votou
		Voto voto = votoRepository.findByPautaIdAndUsuarioId(pauta.get(), form.getUsuarioId());

		if (voto != null) {
			log.info("Usuário já votou");
			return ResponseEntity.ok(new RetornoDto("Usuário já votou", true));
		}

		voto = form.converter(pauta.get());
		votoRepository.save(voto);

		log.info("Voto salvo");
		return ResponseEntity.ok(new RetornoDto("Voto concluído com sucesso", false));
	}

}
