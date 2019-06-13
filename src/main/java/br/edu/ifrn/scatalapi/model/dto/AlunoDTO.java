package br.edu.ifrn.scatalapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Aluno;
import lombok.Data;

@Data
public class AlunoDTO {

	private String nome;
	
	private String matricula;
	
	private String nomeUsual;

	@SerializedName("url_foto")
	@JsonProperty("url_foto")
	private String urlFoto;

	public AlunoDTO(Aluno aluno) {
		this.nome = aluno.getNome();
		this.nomeUsual = aluno.getNomeUsual();
		this.matricula = aluno.getMatricula();
		this.urlFoto = aluno.getUrlFoto();
	}

}
