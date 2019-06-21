package br.edu.ifrn.scatalapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.dao.TutoriaDAO;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.AlunoDTO;

@RestController
@RequestMapping("/tutoria")
public class TutoriaController {

	@GetMapping(value="/{disciplina}/tutores", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<AlunoDTO> get(@PathVariable String disciplina) {
		TutoriaDAO dao = DAOFactory.getTutoriaDAO();
		Tutoria tutoria = dao.buscaPorNomeUsualDaDisciplina(disciplina);
		List<Aluno> alunos = tutoria.getTutores();
		
		List<AlunoDTO> tutores = new ArrayList<>();
		for (Aluno aluno : alunos) {
			AlunoDTO tutor = new AlunoDTO(aluno);
			tutores.add(tutor);
		}
		
		dao.close();
		return tutores;
	}
}
