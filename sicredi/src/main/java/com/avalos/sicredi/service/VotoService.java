package com.avalos.sicredi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.TipoVoto;
import com.avalos.sicredi.model.Voto;
import com.avalos.sicredi.repository.VotoRepository;

@Service
public class VotoService {

	private static final Logger log = LoggerFactory.getLogger(VotoService.class);

	@Autowired
	VotoRepository votoRepository;

	@Autowired
	PautaService pautaService;

	@Autowired
	ValidationService validationService;

	public Voto save(Long pautaId, String usuarioId, TipoVoto tipoVoto) {
		log.info("Salva voto");

		Pauta pauta = pautaService.findById(pautaId);

		validationService.validaSessao(pauta);
		validationService.validaUsuarioVotou(pauta, usuarioId);
		validationService.validaUsuario(usuarioId);

		return votoRepository.save(new Voto(pauta, usuarioId, tipoVoto));
	}

}