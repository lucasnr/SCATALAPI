package br.edu.ifrn.scatalapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class DuvidaDTO {
	private String titulo;
	
	private String descricao;
	
	@SerializedName("aluno_id")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;

}
