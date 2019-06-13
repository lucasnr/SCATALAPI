package br.edu.ifrn.scatalapi.model.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class DuvidaDTO {
	private String titulo;
	
	private String descricao;
	
	// TODO: O Spring não reconhece quando consome
	@SerializedName("aluno_id")
	private Integer idDoAluno;

}
