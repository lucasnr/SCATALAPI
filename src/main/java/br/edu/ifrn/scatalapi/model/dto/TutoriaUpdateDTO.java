package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TutoriaUpdateDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "O nome � um campo obrigatorio")
	private String nome;
	
	@NotNull(message = "O nome usual � um campo obrigatorio")
	@JsonProperty("nome_usual")
	private String nomeUsual;
}
