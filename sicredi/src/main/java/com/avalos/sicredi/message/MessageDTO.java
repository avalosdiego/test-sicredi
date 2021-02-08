package com.avalos.sicredi.message;

public class MessageDTO {

	private Long pautaId;
	private int numeroVotos;
	private int numeroVotosSim;
	private int numeroVotosNao;

	public Long getPautaId() {
		return pautaId;
	}

	public void setPautaId(Long pautaId) {
		this.pautaId = pautaId;
	}

	public int getNumeroVotos() {
		return numeroVotos;
	}

	public void setNumeroVotos(int numeroVotos) {
		this.numeroVotos = numeroVotos;
	}

	public int getNumeroVotosSim() {
		return numeroVotosSim;
	}

	public void setNumeroVotosSim(int numeroVotosSim) {
		this.numeroVotosSim = numeroVotosSim;
	}

	public int getNumeroVotosNao() {
		return numeroVotosNao;
	}

	public void setNumeroVotosNao(int numeroVotosNao) {
		this.numeroVotosNao = numeroVotosNao;
	}

	public String toJSON() {
		return "{\"pautaId\": \"" + pautaId + "\"," + "\"numeroVotos\": \"" + numeroVotos + "\","
				+ "\"numeroVotosSim\": \"" + numeroVotosSim + "\"," + "\"numeroVotosNao\": \"" + numeroVotosNao + "\"}";
	}

}
