package br.edu.ifrn.scatalapi.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RespostaRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "O id do aluno criador da resposta é obrigatório")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;

	private String descricao;

}
