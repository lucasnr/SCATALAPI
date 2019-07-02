package br.edu.ifrn.scatalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "O token informado n�o � v�lido")
public class TokenInvalidoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TokenInvalidoException() {
		super("N�o autorizado");
	}
}
