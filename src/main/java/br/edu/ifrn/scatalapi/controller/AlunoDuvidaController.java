package br.edu.ifrn.scatalapi.controller;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifrn.scatalapi.exception.AlunoComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.FalhaAoSalvarNoBancoDeDadosException;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.DuvidaRequestDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping(value = "/aluno/{id}/duvida", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class AlunoDuvidaController {

	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private PostagemRepository duvidaRepository;

	@Autowired
	private TutoriaRepository tutoriaRepository;
	
	@GetMapping
	public ResponseEntity<Page<DuvidaResponseDTO>> findDuvidasById(@PathVariable Integer id,
			@PageableDefault(page = 0, size = 5, sort = "registro", direction = Direction.DESC) Pageable paginacao) {
		if (! alunoRepository.existsById(id))
			throw new AlunoComIdNaoEncontradoException();
		
		Page<Postagem> duvidas = duvidaRepository.findDuvidasByCriadorId(paginacao, id);
		if (duvidas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<DuvidaResponseDTO> post(@RequestBody @Valid DuvidaRequestDTO duvida, 
			@PathVariable("id") Integer alunoId, UriComponentsBuilder uriBuilder) {
		
		Postagem postagem = buildDuvida(alunoId, duvida);
		boolean salvou = duvidaRepository.save(postagem) != null;
		if (salvou) {
			DuvidaResponseDTO duvidaDTO = new DuvidaResponseDTO(postagem);
			URI location = uriBuilder.path("/duvida/{id}")
					.buildAndExpand(duvidaDTO.getId()).toUri();
			return ResponseEntity.created(location).body(duvidaDTO);
		} 
		
		throw new FalhaAoSalvarNoBancoDeDadosException();
	}

	
	private Postagem buildDuvida(Integer alunoId, DuvidaRequestDTO duvida) {
		Aluno criador = findAlunoOrThrowException(alunoId);
		Tutoria tutoria = findTutoriaOrThrowException(duvida.getIdDaTutoria());
		
		String descricao = duvida.getDescricao();
		String titulo = duvida.getTitulo();

		Postagem postagem = new Postagem(titulo, descricao);
		postagem.setCriador(criador);
		postagem.setTutoria(tutoria);
		return postagem;
	}

	private Tutoria findTutoriaOrThrowException(Integer idDaTutoria) throws TutoriaComIdNaoEncontradoException {
		boolean existe = tutoriaRepository.existsById(idDaTutoria);
		if(existe)
			return new Tutoria(idDaTutoria);
		
		throw new TutoriaComIdNaoEncontradoException();
	}

	private Aluno findAlunoOrThrowException(Integer idDoAluno) throws AlunoComIdNaoEncontradoException {
		boolean existe = alunoRepository.existsById(idDoAluno);
		if(existe)
			return new Aluno(idDoAluno);
			
		throw new AlunoComIdNaoEncontradoException();
	}
}
