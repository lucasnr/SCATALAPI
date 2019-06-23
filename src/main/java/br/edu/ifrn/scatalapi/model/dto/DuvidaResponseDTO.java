package br.edu.ifrn.scatalapi.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Postagem;
import lombok.Data;

@Data
public class DuvidaResponseDTO {

	private Integer id;
	
	private String titulo;
	
	private String descricao;
	
	private Date registro;
	
	@SerializedName("disciplina")
	@JsonProperty("disciplina")
	private String disciplinaUsual;
	
	@SerializedName("aluno_id")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;
	
	public DuvidaResponseDTO(Postagem postagem) {
		this.id = postagem.getId();
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.registro = postagem.getRegistro();
		this.idDoAluno = postagem.getCriador().getId();
		this.disciplinaUsual = postagem.getTutoria().getDisciplina().getNomeUsual();
	}
}
