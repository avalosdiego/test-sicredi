package com.avalos.sicredi.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.model.StatusPauta;

public class PautaDto {

	private Long id;

	private String titulo;

	private String descricao;

	private LocalDateTime dtAbertura;

	private LocalDateTime dtFechamento;

	private StatusPauta status;

	public PautaDto(Pauta pauta) {
		this.id = pauta.getId();
		this.titulo = pauta.getTitulo();
		this.descricao = pauta.getDescricao();
		this.dtAbertura = pauta.getDtAbertura();
		this.dtFechamento = pauta.getDtFechamento();
		this.status = pauta.getStatus();
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

	public StatusPauta getStatus() {
		return status;
	}

	public void setStatus(StatusPauta status) {
		this.status = status;
	}

	public static Page<PautaDto> converter(Page<Pauta> pautas) {
		return pautas.map(PautaDto::new);
	}

}
