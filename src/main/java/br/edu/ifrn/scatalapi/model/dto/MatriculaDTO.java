package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "A matrícula é obrigatória")
	@NotEmpty(message = "A matrícula não pode ser vazia")
	private String valor;
}
