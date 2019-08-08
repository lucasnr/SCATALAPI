package br.edu.ifrn.scatalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "A chave informada no header 'Authorization' n�o � v�lida")
public class ChaveInvalidaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
