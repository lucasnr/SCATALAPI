package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Postagem;
import lombok.Data;

@Data
public class DuvidaResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String titulo;
	
	private String descricao;
	
	private Date registro;
	
	@SerializedName("tutoria_id")
	@JsonProperty("tutoria_id")
	private Integer idDaTutoria;
	
	@SerializedName("aluno_id")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;
	
	public DuvidaResponseDTO(Postagem postagem) {
		this.id = postagem.getId();
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.registro = postagem.getRegistro();
		this.idDoAluno = postagem.getCriador().getId();
		this.idDaTutoria = postagem.getTutoria().getId();
	}
}
