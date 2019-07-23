package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.edu.ifrn.scatalapi.jackson.LocalDateTimeDeserializer;
import br.edu.ifrn.scatalapi.model.Postagem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespostaResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("aluno_id")
	private Integer idDoAluno;

	private Integer id;

	private String descricao;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime registro;

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
