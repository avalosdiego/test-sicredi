package com.avalos.sicredi.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.TipoVoto;
import com.avalos.sicredi.model.Voto;

public class VotoForm {

	@NotBlank
	private String usuarioId;

	@NotNull
	private TipoVoto tipo;

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

	public Voto converterToModel(Pauta pauta) {
		return new Voto(pauta, usuarioId, tipo);
	}

}
