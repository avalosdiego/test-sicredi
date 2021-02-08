package com.avalos.sicredi.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UsuarioDto {

	@Enumerated(EnumType.STRING)
	private StatusUsuario status;

	public StatusUsuario getStatus() {
		return status;
	}

	public void setStatus(StatusUsuario status) {
		this.status = status;
	}

}
