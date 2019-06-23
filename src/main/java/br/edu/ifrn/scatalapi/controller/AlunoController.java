package br.edu.ifrn.scatalapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.AlunoComMatriculaNaoEncontrado;
import br.edu.ifrn.scatalapi.exception.RecursoNaoEncontradoException;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.MatriculaDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
	
	@Autowired
	private AlunoRepository repository;

	@Autowired
	private PostagemRepository postagemRepository;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<AlunoResponseDTO> finddAll(@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao) {
		return repository.findAll(paginacao).map(AlunoResponseDTO::new);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AlunoResponseDTO findById(@PathVariable Integer id) {
		Optional<Aluno> aluno = repository.findById(id);
		if (! aluno.isPresent())
			throw new RecursoNaoEncontradoException();
		
		return new AlunoResponseDTO(aluno.get());
	}
	
	@GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public AlunoResponseDTO findByMatricula(@RequestBody MatriculaDTO matricula) {
		Optional<Aluno> aluno = repository.findByMatricula(matricula.getMatricula());
		if (! aluno.isPresent())
			throw new AlunoComMatriculaNaoEncontrado();
		
		return new AlunoResponseDTO(aluno.get());
	}
	
	@GetMapping(value = "/{id}/duvidas", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<DuvidaResponseDTO> findDuvidasById(@PathVariable Integer id, @PageableDefault(page=0, size=5, sort="registro", direction=Direction.DESC) Pageable paginacao){
		if (! repository.findById(id).isPresent())
			throw new RecursoNaoEncontradoException();
		
		return postagemRepository.findDuvidasByCriadorId(paginacao, id).map(DuvidaResponseDTO::new);
	}
}
