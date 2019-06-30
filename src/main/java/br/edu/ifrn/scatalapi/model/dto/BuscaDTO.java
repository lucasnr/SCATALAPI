package br.edu.ifrn.scatalapi.model.dto;

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

	@NotNull(message = "O conteúdo da busca é obrigatório")
	@NotEmpty(message = "O conteúdo da busca não pode ser vazio")
	private String conteudo;
}
