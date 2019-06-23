package br.edu.ifrn.scatalapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.RecursoNaoEncontradoException;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutorUpdateDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaDetalhadaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaUpdateDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping("/tutoria")
public class TutoriaController {

	@Autowired
	private TutoriaRepository repository;

	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public List<TutoriaResponseDTO> findAll(){
		List<TutoriaResponseDTO> tutorias = repository.findAll().stream().map(TutoriaResponseDTO::new).collect(Collectors.toList());
		return tutorias;
	}
	
	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public TutoriaDetalhadaResponseDTO findById(@PathVariable Integer id) {
		Optional<Tutoria> optional = repository.findById(id);
		if(! optional.isPresent())
			throw new RecursoNaoEncontradoException();
		
		return new TutoriaDetalhadaResponseDTO(optional.get());
	}
	
	@GetMapping(value="/{disciplina}/tutores", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<AlunoResponseDTO> findTutoresByDisciplina(@PathVariable String disciplina) {
		Tutoria tutoria = repository.findByDisciplinaNomeUsual(disciplina);
		if(tutoria == null)
			throw new RecursoNaoEncontradoException();
		
		List<AlunoResponseDTO> tutores = tutoria.getTutores().stream().map(AlunoResponseDTO::new).collect(Collectors.toList());
		return tutores;
	}

	@Autowired
	private AlunoRepository alunoRepository;
	
	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<TutoriaDetalhadaResponseDTO> updateTutores(@RequestBody TutoriaUpdateDTO tutoriaDTO, @PathVariable Integer id) {
		Optional<Tutoria> optional = repository.findById(id);
		if(! optional.isPresent())
			throw new RecursoNaoEncontradoException();
		
		Tutoria tutoria = optional.get();
		List<Aluno> tutores = new ArrayList<>();
		for (TutorUpdateDTO tutor : tutoriaDTO.getTutores()) {
			Aluno aluno = alunoRepository.findByMatricula(tutor.getMatricula());
			tutores.add(aluno);
		}
		
		tutoria.setTutores(tutores);
		return ResponseEntity.ok(new TutoriaDetalhadaResponseDTO(tutoria));
	}
}
