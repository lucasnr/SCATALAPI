package br.edu.ifrn.scatalapi.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BuscaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "O conte�do da busca � obrigat�rio")
	@NotEmpty(message = "O conte�do da busca n�o pode ser vazio")
	private String conteudo;
}
