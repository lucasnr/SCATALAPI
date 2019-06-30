package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Postagem;
import lombok.Data;

@Data
public class RespostaResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("aluno_id")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;

	private Integer id;

	private String descricao;

	private Date registro;

	@SerializedName("duvida_id")
	@JsonProperty("duvida_id")
	private Integer idDaDuvida;
	
	public RespostaResponseDTO(Postagem postagem) {
		this.id = postagem.getId();
		this.descricao = postagem.getDescricao();
		this.registro = postagem.getRegistro();
		this.idDaDuvida = postagem.getPostagemPai().getId();
		this.idDoAluno = postagem.getCriador().getId();
	}
}
