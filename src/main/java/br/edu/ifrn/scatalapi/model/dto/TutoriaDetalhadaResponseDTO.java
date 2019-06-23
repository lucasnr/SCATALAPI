package br.edu.ifrn.scatalapi.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.edu.ifrn.scatalapi.model.Tutoria;
import lombok.Data;

@Data
public class TutoriaDetalhadaResponseDTO{

	private Integer id;

	private String disciplina;

	private String disciplinaUsual;

	private List<AlunoResponseDTO> tutores;
	
	private String curso;

	public TutoriaDetalhadaResponseDTO(Tutoria tutoria) {
		this.id = tutoria.getId();
		this.disciplina = tutoria.getDisciplina().getNome();
		this.disciplinaUsual = tutoria.getDisciplina().getNomeUsual();
		this.tutores = tutoria.getTutores().stream().map(AlunoResponseDTO::new).collect(Collectors.toList());
		this.curso = tutoria.getDisciplina().getCurso().getNome();
	}
}
