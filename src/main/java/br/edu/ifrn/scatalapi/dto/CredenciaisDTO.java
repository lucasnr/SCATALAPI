package br.edu.ifrn.scatalapi.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import lombok.Data;

@Data
public class CredenciaisDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "A matrícula é obrigatória")
	@Length(max = 14, message = "A matrícula não pode conter mais de 14 caracteres")
	@NumberFormat
	private String matricula;

	@NotNull(message = "A senha é obrigatória")
	@NotEmpty(message = "A senha não pode ser vazia")
	private String senha;
}
