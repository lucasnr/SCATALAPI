package br.edu.ifrn.scatalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Essa d�vida n�o existe nessa tutoria")
public class DuvidaNaoEncontradaParaTutoria extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
