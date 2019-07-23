package br.edu.ifrn.scatalapi.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DuvidaUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "O t�tulo da d�vida � obrigat�rio")
	@NotEmpty(message = "O t�tulo da d�vida n�o pode ser vazio")
	private String titulo;
	
	private String descricao;
}
