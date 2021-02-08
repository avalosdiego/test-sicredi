package com.avalos.sicredi.dto;

import org.springframework.data.domain.Page;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.TipoVoto;
import com.avalos.sicredi.model.Voto;

public class VotoDto {

	private Long id;

	private Pauta pauta;

	private String usuarioId;

	private TipoVoto tipo;

	public VotoDto(Voto voto) {
		this.id = voto.getId();
		this.pauta = voto.getPauta();
		this.usuarioId = voto.getUsuarioId();
		this.tipo = voto.getTipo();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pauta getPauta() {
		return pauta;
	}

	public void setPauta(Pauta pauta) {
		this.pauta = pauta;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public TipoVoto getTipo() {
		return tipo;
	}

	public void setTipo(TipoVoto tipo) {
		this.tipo = tipo;
	}

	public static Page<VotoDto> converter(Page<Voto> votos) {
		return votos.map(VotoDto::new);
	}

}
