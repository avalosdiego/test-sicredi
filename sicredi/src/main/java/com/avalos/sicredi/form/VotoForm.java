package com.avalos.sicredi.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.avalos.sicredi.model.TipoVoto;

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

}
