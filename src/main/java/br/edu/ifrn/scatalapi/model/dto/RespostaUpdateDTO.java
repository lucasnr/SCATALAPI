package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RespostaUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "A descrição da resposta é obrigatória")
	@NotEmpty(message = "O descrição da resposta não pode ser vazia")
	private String descricao;
	
}
