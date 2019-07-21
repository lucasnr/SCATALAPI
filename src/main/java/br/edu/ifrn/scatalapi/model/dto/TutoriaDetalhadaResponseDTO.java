package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.edu.ifrn.scatalapi.model.Tutoria;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TutoriaDetalhadaResponseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String nome;

	@JsonProperty("nome_usual")
	private String nomeUsual;

	private List<AlunoResponseDTO> tutores;
	
	private String curso;

	public TutoriaDetalhadaResponseDTO(Tutoria tutoria) {
		this.id = tutoria.getId();
		this.nome = tutoria.getDisciplina().getNome();
		this.nomeUsual = tutoria.getDisciplina().getNomeUsual();
		this.tutores = tutoria.getTutores().stream().map(AlunoResponseDTO::new).collect(Collectors.toList());
		this.curso = tutoria.getDisciplina().getCurso().getNome();
	}
}
