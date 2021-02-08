package com.avalos.sicredi.config.validation.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.avalos.sicredi.config.validation.ErrorFieldDto;
import com.avalos.sicredi.config.validation.ErrorMessageDto;
import com.avalos.sicredi.config.validation.exception.BusinessValidationException;

@RestControllerAdvice
public class ErrorHandler {

	@Autowired
	private MessageSource messageSource;

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorMessageDto handle(MethodArgumentNotValidException ex, WebRequest request) {
		List<ErrorFieldDto> errorFormDtos = new ArrayList<>();

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			errorFormDtos.add(new ErrorFieldDto(e.getField(), mensagem));
		});
		
		ErrorMessageDto errorMessage = new ErrorMessageDto(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		errorMessage.setErrorsField(errorFormDtos);

		return errorMessage;
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ErrorMessageDto resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		return new ErrorMessageDto(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(BusinessValidationException.class)
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	public ErrorMessageDto businessValidationException(Exception ex, WebRequest request) {
		return new ErrorMessageDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessageDto globalExceptionHandler(Exception ex, WebRequest request) {
		return new ErrorMessageDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
	}

}
