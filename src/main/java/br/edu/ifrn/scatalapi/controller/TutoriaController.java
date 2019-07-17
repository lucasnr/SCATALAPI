package br.edu.ifrn.scatalapi.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.AlunoComMatriculaNaoEncontrado;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
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
@RequestMapping(value = "/tutoria", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class TutoriaController {

	@Autowired
	private TutoriaRepository repository;

	@Autowired
	private PostagemRepository postagemRepository;

	@Autowired
	private AlunoRepository alunoRepository;

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
		Tutoria tutoria = getTutoriaOrThrowException(id);
		return new TutoriaDetalhadaResponseDTO(tutoria);
	}

	@GetMapping("/{id}/tutores")
	@Cacheable(value = "tutores")
	public ResponseEntity<List<AlunoResponseDTO>> findTutoresById(@PathVariable Integer id) {
		Tutoria tutoria = getTutoriaOrThrowException(id);
		List<Aluno> tutores = tutoria.getTutores();
		if (tutores.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(tutores.stream().map(AlunoResponseDTO::new).collect(Collectors.toList()));
	}

	@GetMapping("/{id}/duvidas")
	public ResponseEntity<Page<DuvidaResponseDTO>> findDuvidasById(@PathVariable Integer id,
			@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao) {
		Page<Postagem> duvidas = postagemRepository.findDuvidasByTutoriaId(paginacao, id);
		if (duvidas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}

	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@CacheEvict(value= {"tutorias", "tutoria", "tutores"}, allEntries=true)
	public ResponseEntity<TutoriaDetalhadaResponseDTO> updateTutoresById(@PathVariable Integer id,
			@RequestBody @Valid TutoriaUpdateDTO tutoriaDTO) {
		
		Tutoria tutoria = getTutoriaOrThrowException(id);
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
	
	private Tutoria getTutoriaOrThrowException(Integer id) throws TutoriaComIdNaoEncontradaException {
		return repository.findById(id).orElseThrow(TutoriaComIdNaoEncontradaException::new);
	}
}
