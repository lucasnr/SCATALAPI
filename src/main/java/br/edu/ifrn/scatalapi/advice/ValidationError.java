package br.edu.ifrn.scatalapi.advice;

import org.springframework.validation.FieldError;

import lombok.Data;

@Data
public class ValidationError {

	private final String field;
	private final String message;

	public ValidationError(FieldError fieldError) {
		this.field = fieldError.getField();
		this.message = fieldError.getDefaultMessage();
	}
}
