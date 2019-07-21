package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TutoriaTutoresUpdateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "A lista de tutores da tutoria é obrigatória")
	@NotEmpty(message = "A lista de tutores da tutoria não pode ser vazia")
	private List<MatriculaDTO> tutores;
}
