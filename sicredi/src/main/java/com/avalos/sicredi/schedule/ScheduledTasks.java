package com.avalos.sicredi.schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.avalos.sicredi.message.ActiveMQPublisher;
import com.avalos.sicredi.message.MessageDTO;
import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.StatusPauta;
import com.avalos.sicredi.model.TipoVoto;
import com.avalos.sicredi.repository.PautaRepository;
import com.avalos.sicredi.repository.VotoRepository;

@Component
public class ScheduledTasks {

	@Autowired
	private PautaRepository pautaRepository;

	@Autowired
	private VotoRepository votoRepository;

	@Autowired
	private ActiveMQPublisher activeMQPublisher;

	@Scheduled(fixedRate = 5000)
	public void verificaPautas() {
		List<Pauta> pautas = pautaRepository.findByStatus(StatusPauta.OPENED);

		for (Pauta pauta : pautas) {
			if (pauta.getDtFechamento().isBefore(LocalDateTime.now())) {
				pauta.setStatus(StatusPauta.CLOSED);
				pautaRepository.save(pauta);

				MessageDTO messageDto = new MessageDTO();
				messageDto.setPautaId(pauta.getId());
				messageDto.setNumeroVotos(pauta.getVotos().size());
				messageDto.setNumeroVotosNao(votoRepository.findTipoVotoAndPautaId(TipoVoto.NAO, pauta).size());
				messageDto.setNumeroVotosSim(votoRepository.findTipoVotoAndPautaId(TipoVoto.SIM, pauta).size());

				activeMQPublisher.publicarMensagem(messageDto.toJSON());
			}
		}
	}

}
