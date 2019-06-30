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

	@NotNull(message = "A descri��o da resposta � obrigat�ria")
	@NotEmpty(message = "O descri��o da resposta n�o pode ser vazia")
	private String descricao;
	
}
