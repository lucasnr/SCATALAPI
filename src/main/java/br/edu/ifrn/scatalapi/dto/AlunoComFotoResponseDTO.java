package br.edu.ifrn.scatalapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.edu.ifrn.scatalapi.model.Aluno;
import lombok.Data;

@Data
public class AlunoComFotoResponseDTO {

	@JsonProperty("url_foto")
	private String urlFoto;
	
	private String matricula;
	
	public AlunoComFotoResponseDTO(Aluno aluno) {
		this.urlFoto = aluno.getUrlFoto();
		this.matricula = aluno.getMatricula();
	}
}
