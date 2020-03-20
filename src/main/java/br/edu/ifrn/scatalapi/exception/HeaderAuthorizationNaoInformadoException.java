package br.edu.ifrn.scatalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "O header 'Authorization' não foi informado")
public class HeaderAuthorizationNaoInformadoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
