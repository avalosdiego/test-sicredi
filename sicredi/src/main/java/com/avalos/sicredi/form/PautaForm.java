package com.avalos.sicredi.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.avalos.sicredi.model.Pauta;
import com.avalos.sicredi.repository.PautaRepository;

public class PautaForm {

	@NotNull
	@NotEmpty
	@Length(min = 5)
	private String titulo;

	@NotNull
	@NotEmpty
	@Length(min = 5)
	private String descricao;

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

	public Pauta converter() {
		return new Pauta(titulo, descricao);
	}

	public Pauta atualizar(Long id, PautaRepository pautaRepository) {
		Pauta pauta = pautaRepository.getOne(id);

		pauta.setTitulo(this.titulo);
		pauta.setDescricao(this.descricao);

		return pauta;
	}

}
