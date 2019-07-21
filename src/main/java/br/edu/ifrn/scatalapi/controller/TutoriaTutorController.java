package br.edu.ifrn.scatalapi.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.AlunoComMatriculaNaoEncontrado;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaDetalhadaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaTutoresUpdateDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping(value = "/tutoria/{id}/tutor", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class TutoriaTutorController {

	@Autowired
	private TutoriaRepository tutoriaRepository;
	
	@Autowired
	private AlunoRepository alunoRepository;

	@GetMapping
	@Cacheable(value = "tutores")
	public ResponseEntity<List<AlunoResponseDTO>> findTutoresById(@PathVariable Integer id) {
		Tutoria tutoria = findTutoriaOrThrowException(id);
		List<Aluno> tutores = tutoria.getTutores();
		if (tutores.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(tutores.stream().map(AlunoResponseDTO::new).collect(Collectors.toList()));
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@CacheEvict(value= {"tutorias", "tutoria", "tutores"}, allEntries=true)
	public ResponseEntity<TutoriaDetalhadaResponseDTO> updateTutoresById(@PathVariable Integer id,
			@RequestBody @Valid TutoriaTutoresUpdateDTO tutoriaDTO) {
		
		Tutoria tutoria = findTutoriaOrThrowException(id);
		try {
			List<Aluno> tutores = tutoriaDTO.getTutores().stream()
					.map(matricula -> alunoRepository.findByMatricula(matricula.getValor()))
					.map(Optional::get)
					.collect(Collectors.toList());

			tutoria.setTutores(tutores);
			return ResponseEntity.ok(new TutoriaDetalhadaResponseDTO(tutoria));
		} catch (Exception e) {
			throw new AlunoComMatriculaNaoEncontrado();
		}
	}
	
	private Tutoria findTutoriaOrThrowException(Integer id) throws TutoriaComIdNaoEncontradoException {
		return tutoriaRepository.findById(id).orElseThrow(TutoriaComIdNaoEncontradoException::new);
	}
}
