package com.avalos.sicredi.config.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessValidationException extends RuntimeException {

	private static final long serialVersionUID = 7992904489502842099L;

	public BusinessValidationException() {
		this("EntityRepresentationModel bussiness validation!");
	}

	public BusinessValidationException(String message) {
		this(message, null);
	}

	public BusinessValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
