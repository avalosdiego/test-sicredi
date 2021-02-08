package com.avalos.sicredi.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Voto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Pauta pauta;

	private String usuarioId;

	@Enumerated(EnumType.STRING)
	private TipoVoto tipo;

	public Voto() {

	}

	public Voto(Pauta pauta, String usuarioId, TipoVoto tipo) {
		super();
		this.pauta = pauta;
		this.usuarioId = usuarioId;
		this.tipo = tipo;
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

}
