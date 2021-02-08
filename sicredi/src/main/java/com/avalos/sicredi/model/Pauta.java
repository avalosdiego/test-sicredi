package com.avalos.sicredi.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Pauta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	private String descricao;

	private LocalDateTime dtAbertura;

	private LocalDateTime dtFechamento;

	@Enumerated(EnumType.STRING)
	private StatusPauta status = StatusPauta.WAITING;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pauta", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Voto> votos;

	public Pauta() {
		super();
	}

	public Pauta(String titulo, String descricao) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDateTime getDtAbertura() {
		return dtAbertura;
	}

	public void setDtAbertura(LocalDateTime dtAbertura) {
		this.dtAbertura = dtAbertura;
	}

	public LocalDateTime getDtFechamento() {
		return dtFechamento;
	}

	public void setDtFechamento(LocalDateTime dtFechamento) {
		this.dtFechamento = dtFechamento;
	}

	public List<Voto> getVotos() {
		return votos;
	}

	public void setVotos(List<Voto> votos) {
		this.votos = votos;
	}

	public StatusPauta getStatus() {
		return status;
	}

	public void setStatus(StatusPauta status) {
		this.status = status;
	}

}
