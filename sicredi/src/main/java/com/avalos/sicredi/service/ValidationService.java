package com.avalos.sicredi.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.avalos.sicredi.config.validation.exception.BusinessValidationException;
import com.avalos.sicredi.dto.StatusUsuario;
import com.avalos.sicredi.dto.UsuarioDto;
import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.Voto;
import com.avalos.sicredi.repository.VotoRepository;

@Service
public class ValidationService {

	private static final Logger log = LoggerFactory.getLogger(ValidationService.class);

	@Autowired
	VotoRepository votoRepository;

	public void validaUsuario(String usuarioId) {
		try {
			log.info("Conectando com heroku para validar usuário");

			RestTemplate restTemplate = new RestTemplate();
			String uri = "https://user-info.herokuapp.com/users/" + usuarioId;

			UsuarioDto usuarioDto = restTemplate.getForObject(uri, UsuarioDto.class);

			if (usuarioDto != null && usuarioDto.getStatus() != null
					&& usuarioDto.getStatus().equals(StatusUsuario.UNABLE_TO_VOTE)) {
				throw new BusinessValidationException("Usuário " + usuarioId + " não está apto para votar");
			}
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new ResourceNotFoundException("Usuário " + usuarioId + " inexistente");
			}
		} catch (Exception e) {
			throw e;
		}
		log.info("Usuário validado com sucesso no heroku");
	}

	public void validaSessao(Pauta pauta) {
		if (pauta.getDtFechamento() == null) {
			throw new BusinessValidationException("Sessão de votação não foi iniciada");
		}

		if (pauta.getDtFechamento().isBefore(LocalDateTime.now())) {
			throw new BusinessValidationException("Sessão de votação já encerrada");
		}
	}

	public void validaUsuarioVotou(Pauta pauta, String usuarioId) {
		Voto voto = votoRepository.findByPautaIdAndUsuarioId(pauta, usuarioId);

		if (voto != null) {
			throw new BusinessValidationException("Usuário já votou");
		}
	}
	
}
