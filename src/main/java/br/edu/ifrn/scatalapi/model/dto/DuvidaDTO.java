package br.edu.ifrn.scatalapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Postagem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DuvidaDTO {
	private String titulo;
	
	private String descricao;
	
	@SerializedName("aluno_id")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;

	public DuvidaDTO(Postagem postagem) {
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.idDoAluno = postagem.getCriador().getId();
	}
}
