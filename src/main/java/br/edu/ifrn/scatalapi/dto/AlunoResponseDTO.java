package br.edu.ifrn.scatalapi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.edu.ifrn.scatalapi.jackson.LocalDateTimeDeserializer;
import br.edu.ifrn.scatalapi.model.Aluno;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlunoResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String nome;

	private String matricula;

	@JsonProperty("nome_usual")
	private String nomeUsual;

	@JsonProperty("url_foto")
	private String urlFoto;

	private String email;

	private String curso;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime registro;

	public AlunoResponseDTO(Aluno aluno) {
		this.id = aluno.getId();
		this.nome = aluno.getNome();
		this.nomeUsual = aluno.getNomeUsual();
		this.matricula = aluno.getMatricula();
		this.urlFoto = aluno.getUrlFoto();
		this.email = new ArrayList<>(aluno.getEmails()).get(0).getEndereco();
		this.curso = aluno.getCurso().getNome();
		this.registro = aluno.getRegistro();
	}

}
