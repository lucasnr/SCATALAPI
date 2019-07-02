package br.edu.ifrn.scatalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "O token de autorização não foi informado")
public class TokenNaoInformadoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
