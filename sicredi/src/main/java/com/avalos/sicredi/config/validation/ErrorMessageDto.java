package com.avalos.sicredi.config.validation;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorMessageDto {

	private int statusCode;
	private LocalDateTime timestamp;
	private String message;
	private String description;
	private List<ErrorFieldDto> errorsField;

	public ErrorMessageDto(int statusCode, LocalDateTime timestamp, String message, String description) {
		this.statusCode = statusCode;
		this.timestamp = timestamp;
		this.message = message;
		this.description = description;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDescription() {
		return description;
	}

	public List<ErrorFieldDto> getErrorsField() {
		return errorsField;
	}

	public void setErrorsField(List<ErrorFieldDto> errorsField) {
		this.errorsField = errorsField;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
