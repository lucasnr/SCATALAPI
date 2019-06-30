package br.edu.ifrn.scatalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.NOT_FOUND, reason="O id informado não pertence a nenhuma dúvida cadastrada")
public class DuvidaComIdNaoEncontradaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
