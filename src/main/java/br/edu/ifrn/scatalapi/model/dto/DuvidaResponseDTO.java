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
public class DuvidaResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String titulo;
	
	private String descricao;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime registro;
	
	@JsonProperty("tutoria_id")
	private Integer idDaTutoria;
	
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
