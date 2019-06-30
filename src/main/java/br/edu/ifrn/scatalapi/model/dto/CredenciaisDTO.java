package br.edu.ifrn.scatalapi.model.dto;

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

	@NotNull(message = "A matr�cula � obrigat�ria")
	@Length(max = 14, message = "A matr�cula n�o pode conter mais de 14 caracteres")
	@NumberFormat
	private String matricula;

	@NotNull(message = "A senha � obrigat�ria")
	@NotEmpty(message = "A senha n�o pode ser vazia")
	private String senha;
}
