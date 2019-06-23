package br.edu.ifrn.scatalapi.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.AlunoComMatriculaNaoEncontrado;
import br.edu.ifrn.scatalapi.exception.RecursoNaoEncontradoException;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaDetalhadaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaUpdateDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping("/tutoria")
public class TutoriaController {

	@Autowired
	private TutoriaRepository repository;

	@Autowired
	private PostagemRepository postagemRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TutoriaResponseDTO> findAll() {
		List<TutoriaResponseDTO> tutorias = repository.findAll().stream().map(TutoriaResponseDTO::new).collect(Collectors.toList());
		return tutorias;
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TutoriaDetalhadaResponseDTO findById(@PathVariable Integer id) {
		Optional<Tutoria> optional = repository.findById(id);
		if (!optional.isPresent())
			throw new RecursoNaoEncontradoException();

		return new TutoriaDetalhadaResponseDTO(optional.get());
	}

	@GetMapping(value = "/{disciplina}/tutores", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AlunoResponseDTO> findTutoresByDisciplina(@PathVariable String disciplina) {
		Tutoria tutoria = repository.findByDisciplinaNomeUsual(disciplina);
		if (tutoria == null)
			throw new RecursoNaoEncontradoException();

		List<AlunoResponseDTO> tutores = tutoria.getTutores().stream().map(AlunoResponseDTO::new).collect(Collectors.toList());
		return tutores;
	}

	@GetMapping(value = "/{disciplina}/duvidas", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<DuvidaResponseDTO> findDuvidasByDisciplina(@PathVariable String disciplina,
			@RequestParam Integer offset, @RequestParam Integer number) {
		Pageable paginacao = PageRequest.of(offset, number);
		Page<Postagem> duvidas = postagemRepository.findDuvidasByDisciplina(paginacao, disciplina);
		return duvidas.map(DuvidaResponseDTO::new);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<TutoriaDetalhadaResponseDTO> updateTutores(@RequestBody TutoriaUpdateDTO tutoriaDTO,
			@PathVariable Integer id) {
		Optional<Tutoria> optional = repository.findById(id);
		if (!optional.isPresent())
			throw new RecursoNaoEncontradoException();

		Tutoria tutoria = optional.get();
		try {
			List<Aluno> tutores = tutoriaDTO.getTutores().stream()
					.map(m -> alunoRepository.findByMatricula(m.getMatricula())).map(Optional::get)
					.collect(Collectors.toList());

			tutoria.setTutores(tutores);
			return ResponseEntity.ok(new TutoriaDetalhadaResponseDTO(tutoria));
		} catch (Exception e) {
			throw new AlunoComMatriculaNaoEncontrado();
		}
	}
}
