package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DuvidaRequestDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "O t�tulo da d�vida � obrigat�rio")
	@NotEmpty(message = "O t�tulo da d�vida n�o pode ser vazio")
	private String titulo;

	private String descricao;

	@NotNull(message = "O id da tutoria da d�vida � obrigat�rio")
	@JsonProperty("tutoria_id")
	private Integer idDaTutoria;
	
	@NotNull(message = "O id do aluno criador da d�vida � obrigat�rio")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;

}
