package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import br.edu.ifrn.scatalapi.model.Tutoria;
import lombok.Data;

@Data
public class TutoriaResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String disciplina;
	
	@SerializedName("disciplina_usual")
	@JsonProperty("disciplina_usual")
	private String disciplinaUsual;
	
	public TutoriaResponseDTO(Tutoria tutoria) {
		this.id = tutoria.getId();
		this.disciplina = tutoria.getDisciplina().getNome();
		this.disciplinaUsual = tutoria.getDisciplina().getNomeUsual();
	}
}
