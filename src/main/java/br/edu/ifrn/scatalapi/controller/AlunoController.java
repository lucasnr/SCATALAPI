package br.edu.ifrn.scatalapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.AlunoComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.AlunoComMatriculaNaoEncontrado;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;

@RestController
@RequestMapping(value = "/aluno", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class AlunoController {

	@Autowired
	private AlunoRepository repository;

	@Autowired
	private PostagemRepository postagemRepository;

	@GetMapping
	public ResponseEntity<Page<AlunoResponseDTO>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "registro", direction = Direction.DESC) Pageable paginacao) {
		
		Page<Aluno> alunos = repository.findAll(paginacao);
		if(alunos.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(alunos.map(AlunoResponseDTO::new));
	}

	@GetMapping(value = "/{id}")
	@Cacheable(value = "aluno")
	public AlunoResponseDTO findById(@PathVariable Integer id) {
		Aluno aluno = repository.findById(id).orElseThrow(AlunoComIdNaoEncontradoException::new);
		return new AlunoResponseDTO(aluno);
	}

	@GetMapping(value = "/busca/{matricula}")
	@Cacheable(value = "alunoByMatricula")
	public AlunoResponseDTO findByMatricula(@PathVariable String matricula) {
		Aluno aluno = repository.findByMatricula(matricula)
				.orElseThrow(AlunoComMatriculaNaoEncontrado::new);
		return new AlunoResponseDTO(aluno);
	}

	@GetMapping(value = "/{id}/duvidas")
	public ResponseEntity<Page<DuvidaResponseDTO>> findDuvidasById(@PathVariable Integer id,
			@PageableDefault(page = 0, size = 5, sort = "registro", direction = Direction.DESC) Pageable paginacao) {
		if (! repository.existsById(id))
			throw new AlunoComIdNaoEncontradoException();
		
		Page<Postagem> duvidas = postagemRepository.findDuvidasByCriadorId(paginacao, id);
		if (duvidas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}
}
