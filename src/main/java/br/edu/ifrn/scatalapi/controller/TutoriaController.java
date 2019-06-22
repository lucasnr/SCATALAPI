package br.edu.ifrn.scatalapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.AlunoDTO;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping("/tutoria")
public class TutoriaController {

	@Autowired
	private TutoriaRepository repository;
	
	@GetMapping(value="/{disciplina}/tutores", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<AlunoDTO> get(@PathVariable String disciplina) {
		Tutoria tutoria = repository.findByDisciplinaNomeUsual(disciplina);
		List<Aluno> alunos = tutoria.getTutores();
		
		List<AlunoDTO> tutores = new ArrayList<>();
		for (Aluno aluno : alunos) {
			AlunoDTO tutor = new AlunoDTO(aluno);
			tutores.add(tutor);
		}
		return tutores;
	}
}
