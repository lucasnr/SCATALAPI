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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifrn.scatalapi.exception.AlunoComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.DuvidaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.exception.FalhaAoSalvarNoBancoDeDadosException;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.DuvidaRequestDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaUpdateDTO;
import br.edu.ifrn.scatalapi.model.dto.RespostaRequestDTO;
import br.edu.ifrn.scatalapi.model.dto.RespostaResponseDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping("/duvida")
@AutenticadoRequired
public class DuvidaController {

	@Autowired
	private PostagemRepository repository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private TutoriaRepository tutoriaRepository;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DuvidaResponseDTO findById(@PathVariable Integer id) {
		Postagem postagem = getDuvidaOrThrowException(id);
		return new DuvidaResponseDTO(postagem);
	}
	
	@GetMapping(value = "/{id}/respostas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<RespostaResponseDTO>> findRespostasById(@PathVariable Integer id,
			@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao) {
		Postagem postagem = getDuvidaOrThrowException(id);
		Page<Postagem> respostas = repository.findRespostasByDuvidaId(paginacao, postagem.getId());
		if(respostas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(respostas.map(RespostaResponseDTO::new));
	}
	
	@GetMapping(value = "/busca/{conteudo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<DuvidaResponseDTO>> findBySearch(@PathVariable String conteudo, 
			@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao){
		Page<Postagem> duvidasEncontradas = repository.findAnyDuvidas(paginacao, conteudo);
		if (duvidasEncontradas.isEmpty()) 
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidasEncontradas.map(DuvidaResponseDTO::new));
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<DuvidaResponseDTO> post(@RequestBody @Valid DuvidaRequestDTO duvida, 
			UriComponentsBuilder uriBuilder) {
		Aluno aluno = findAluno(duvida.getIdDoAluno());
		Tutoria tutoria = findTutoriaDaDuvida(duvida);
		Postagem postagem = createPostagem(duvida, aluno, tutoria);

		boolean salvou = repository.save(postagem) != null;
		if (salvou) {
			DuvidaResponseDTO duvidaDTO = new DuvidaResponseDTO(postagem);
			URI location = uriBuilder.path("/duvida/{id}")
					.buildAndExpand(duvidaDTO.getId()).toUri();
			return ResponseEntity.created(location).body(duvidaDTO);
		} 
		
		throw new FalhaAoSalvarNoBancoDeDadosException();
	}
	
	@PostMapping(value = "/{id}/resposta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<RespostaResponseDTO> postResposta(@PathVariable Integer id, 
			@RequestBody @Valid RespostaRequestDTO resposta, UriComponentsBuilder uriBuilder){
		Aluno criador = findAluno(resposta.getIdDoAluno());
		Postagem duvida = getDuvidaOrThrowException(id);
		
		Postagem postagem = new Postagem(null, resposta.getDescricao());
		postagem.setCriador(criador);
		postagem.setPostagemPai(duvida);
		postagem.setTutoria(duvida.getTutoria());
		
		Postagem salvo = repository.save(postagem);
		if (salvo == null)
			throw new FalhaAoSalvarNoBancoDeDadosException();
		
		RespostaResponseDTO responseDTO = new RespostaResponseDTO(postagem);
		URI location = uriBuilder.path("/resposta/{id}").buildAndExpand(responseDTO.getId()).toUri();
		return ResponseEntity.created(location).body(responseDTO);
	}
	
	@PatchMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<?> updateById(@PathVariable Integer id, 
			@RequestBody @Valid DuvidaUpdateDTO duvida){
		Postagem postagem = getDuvidaOrThrowException(id);
		postagem.setTitulo(duvida.getTitulo());
		postagem.setDescricao(duvida.getDescricao());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		try {
			repository.deleteById(id);			
			return ResponseEntity.noContent().build();
		} catch (Exception e) {			
			throw new DuvidaComIdNaoEncontradaException();
		}
	}
	
	private Postagem getDuvidaOrThrowException(Integer id) {
		return repository.findDuvidaById(id)
				.orElseThrow(DuvidaComIdNaoEncontradaException::new);
	}
	
	private Postagem createPostagem(DuvidaRequestDTO duvida, Aluno criador, Tutoria tutoria) {
		String descricao = duvida.getDescricao();
		String titulo = duvida.getTitulo();

		Postagem postagem = new Postagem(titulo, descricao);
		postagem.setCriador(criador);
		postagem.setTutoria(tutoria);
		return postagem;
	}

	private Tutoria findTutoriaDaDuvida(DuvidaRequestDTO duvida) throws TutoriaComIdNaoEncontradaException {
		Integer id = duvida.getIdDaTutoria();
		boolean existe = tutoriaRepository.existsById(id);
		if(existe)
			return new Tutoria(id);
		
		throw new TutoriaComIdNaoEncontradaException();
	}

	private Aluno findAluno(Integer idDoAluno) throws AlunoComIdNaoEncontradoException {
		boolean existe = alunoRepository.existsById(idDoAluno);
		if(existe)
			return new Aluno(idDoAluno);
			
		throw new AlunoComIdNaoEncontradoException();
	}
}
