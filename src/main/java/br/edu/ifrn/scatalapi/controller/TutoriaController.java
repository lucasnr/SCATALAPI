package br.edu.ifrn.scatalapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dto.TutoriaDetalhadaResponseDTO;
import br.edu.ifrn.scatalapi.dto.TutoriaResponseDTO;
import br.edu.ifrn.scatalapi.dto.TutoriaUpdateDTO;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Disciplina;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping(value = "/tutoria", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class TutoriaController {

	@Autowired
	private TutoriaRepository repository;

	@GetMapping
	@Cacheable(value = "tutorias")
	public List<TutoriaResponseDTO> findAll() {
		List<TutoriaResponseDTO> tutorias = repository.findAll()
				.stream().map(TutoriaResponseDTO::new).collect(Collectors.toList());
		return tutorias;
	}

	@GetMapping("/{id}")
	@Cacheable(value = "tutoria")
	public TutoriaDetalhadaResponseDTO findById(@PathVariable Integer id) {
		Tutoria tutoria = findTutoriaOrThrowException(id);
		return new TutoriaDetalhadaResponseDTO(tutoria);
	}
	
	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@CacheEvict(value = {"tutorias", "tutoria"}, allEntries = true)
	public TutoriaDetalhadaResponseDTO uptadeById(@PathVariable Integer id, @RequestBody @Valid TutoriaUpdateDTO tutoriaDTO) {
		Tutoria tutoria = findTutoriaOrThrowException(id);
		Disciplina disciplina = tutoria.getDisciplina();
		disciplina.setNome(tutoriaDTO.getNome());
		disciplina.setNomeUsual(tutoriaDTO.getNomeUsual());
		return new TutoriaDetalhadaResponseDTO(tutoria);
	}

	private Tutoria findTutoriaOrThrowException(Integer id) throws TutoriaComIdNaoEncontradoException {
		return repository.findById(id).orElseThrow(TutoriaComIdNaoEncontradoException::new);
	}
}
