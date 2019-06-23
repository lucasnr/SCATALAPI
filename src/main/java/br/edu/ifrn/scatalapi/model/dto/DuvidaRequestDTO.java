package br.edu.ifrn.scatalapi.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Postagem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DuvidaRequestDTO {
	
	@NotNull @NotEmpty @Length(min=5)
	private String titulo;

	@NotNull @NotEmpty @Length(min=40)
	private String descricao;
	
	@SerializedName("disciplina")
	@JsonProperty("disciplina")
	private String disciplinaUsual;
	
	@NotNull @Pattern(regexp="\\d*") 
	@SerializedName("aluno_id")
	@JsonProperty("aluno_id")
	private Integer idDoAluno;

	public DuvidaRequestDTO(Postagem postagem) {
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.idDoAluno = postagem.getCriador().getId();
		this.disciplinaUsual = postagem.getTutoria().getDisciplina().getNomeUsual();
	}
}
