package br.edu.ifrn.scatalapi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.edu.ifrn.scatalapi.jackson.LocalDateTimeDeserializer;
import br.edu.ifrn.scatalapi.model.Aluno;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlunoResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "O ID gerado do aluno")
	private Integer id;

    @ApiModelProperty(notes = "O nome do aluno")
	private String nome;

    @ApiModelProperty(notes = "A matrícula do aluno com o SUAP")
	private String matricula;

    @ApiModelProperty(notes = "O nome usual do aluno com o SUAP")
	@JsonProperty("nome_usual")
	private String nomeUsual;

    @ApiModelProperty(notes = "O caminho da foto do aluno")
	@JsonProperty("url_foto")
	private String urlFoto;

    @ApiModelProperty(notes = "O email principal do aluno")
	private String email;

    @ApiModelProperty(notes = "O nome do curso do aluno")
	private String curso;

    @ApiModelProperty(notes = "A data e hora do registro do aluno")
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
