package br.edu.ifrn.scatalapi.dto;

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

	@NotNull(message = "O título da dúvida é obrigatório")
	@NotEmpty(message = "O título da dúvida não pode ser vazio")
	private String titulo;

	private String descricao;

	@NotNull(message = "O id da tutoria da dúvida é obrigatório")
	@JsonProperty("tutoria_id")
	private Integer idDaTutoria;

}
