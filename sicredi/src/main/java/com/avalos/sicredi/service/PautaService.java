package com.avalos.sicredi.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.StatusPauta;
import com.avalos.sicredi.repository.PautaRepository;

@Service
public class PautaService {

	private static final Logger log = LoggerFactory.getLogger(PautaService.class);

	private static final String PAUTA_NAO_ENCONTRADA = "Pauta não encontrada ID: ";

	@Autowired
	PautaRepository pautaRepository;

	public Page<Pauta> findAllWithPagination(Pageable pageable) {
		log.info("Busca pautas com paginação");
		return pautaRepository.findAll(pageable);
	}

	public Pauta save(Pauta pauta) {
		log.info("Salva pauta");
		return pautaRepository.save(pauta);
	}

	public Pauta findById(Long id) {
		log.info("Busca pauta");
		return pautaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PAUTA_NAO_ENCONTRADA + id));
	}

	public void deleteById(Long id) {
		log.info("Deleta pauta");
		Pauta pauta = findById(id);
		pautaRepository.deleteById(pauta.getId());
	}

	public Pauta abrirSessao(Long id, Long tempo) {
		log.info("Abre sessão");
		Pauta pauta = findById(id);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime fechamento = now.plusMinutes(tempo);

		pauta.setDtAbertura(now);
		pauta.setDtFechamento(fechamento);
		pauta.setStatus(StatusPauta.OPENED);

		return pauta;
	}

}