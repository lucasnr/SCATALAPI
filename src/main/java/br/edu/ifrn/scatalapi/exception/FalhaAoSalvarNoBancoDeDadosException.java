package br.edu.ifrn.scatalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR, reason="Falha ao cadastrar no banco de dados")
public class FalhaAoSalvarNoBancoDeDadosException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
