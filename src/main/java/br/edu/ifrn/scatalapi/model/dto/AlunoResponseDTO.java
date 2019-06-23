package br.edu.ifrn.scatalapi.model.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Aluno;
import lombok.Data;

@Data
public class AlunoResponseDTO {

	private Integer id;
	
	private String nome;
	
	private String matricula;
	
	private String nomeUsual;

	@SerializedName("url_foto")
	@JsonProperty("url_foto")
	private String urlFoto;

	private String email;

	private String curso;

	public AlunoResponseDTO(Aluno aluno) {
		this.id = aluno.getId();
		this.nome = aluno.getNome();
		this.nomeUsual = aluno.getNomeUsual();
		this.matricula = aluno.getMatricula();
		this.urlFoto = aluno.getUrlFoto();
		this.email = new ArrayList<>(aluno.getEmails()).get(0).getEndereco();
		this.curso = aluno.getCurso().getNome();
	}

}
