package com.avalos.sicredi.config.validation;

public class RetornoDto {

	private String message;

	private boolean isError;

	public RetornoDto(String message, boolean isError) {
		super();
		this.message = message;
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

}
