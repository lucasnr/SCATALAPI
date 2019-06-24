package br.edu.ifrn.scatalapi.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifrn.scatalapi.exception.FalhaAoSalvarNoBancoDeDadosException;
import br.edu.ifrn.scatalapi.exception.RecursoNaoEncontradoException;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.BuscaDTO;
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
	public List<RespostaResponseDTO> findRespostasById(@PathVariable Integer id) {
		Postagem postagem = getDuvidaOrThrowException(id);
		return postagem.getRespostas().
				stream().map(RespostaResponseDTO::new).collect(Collectors.toList());
	}
	
	@GetMapping(value = "/busca", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<DuvidaResponseDTO> findAny(@RequestBody BuscaDTO busca, 
			@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao){
		return repository.findAnyDuvidas(paginacao, busca.getConteudo()).map(DuvidaResponseDTO::new);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<DuvidaResponseDTO> postDuvida(@RequestBody DuvidaRequestDTO duvida, UriComponentsBuilder uriBuilder) {
		Aluno aluno = findAlunoIfExists(duvida.getIdDoAluno());
		// if(aluno == null)
		// throw exception
		Tutoria tutoria = findTutoriaDaDuvida(duvida);
		Postagem postagem = createPostagem(duvida, aluno, tutoria);

		boolean salvou = repository.save(postagem) != null;
		if (salvou) {
			DuvidaResponseDTO duvidaDTO = new DuvidaResponseDTO(postagem);
			URI location = uriBuilder.path("/duvida/{id}")
					.buildAndExpand(duvidaDTO.getId()).toUri();
			return ResponseEntity.created(location).body(duvidaDTO);
		} else
			throw new FalhaAoSalvarNoBancoDeDadosException();
	}
	
	@PostMapping(value = "/{id}/resposta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<RespostaResponseDTO> postResposta(@PathVariable Integer id, @RequestBody RespostaRequestDTO resposta,
			UriComponentsBuilder uriBuilder){
		Postagem duvida = getDuvidaOrThrowException(id);
		Aluno criador = findAlunoIfExists(resposta.getIdDoAluno());
		
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
	
	@PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<DuvidaResponseDTO> updateDuvidaById(@RequestBody DuvidaUpdateDTO duvida, @PathVariable Integer id){
		Postagem postagem = getDuvidaOrThrowException(id);
		postagem.setTitulo(duvida.getTitulo());
		postagem.setDescricao(duvida.getDescricao());
		return ResponseEntity.ok(new DuvidaResponseDTO(postagem));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteDuvidaById(@PathVariable Integer id){
		try {
			repository.deleteById(id);			
			return ResponseEntity.ok().build();
		} catch (Exception e) {			
			throw new RecursoNaoEncontradoException();
		}
	}
	
	private Postagem getDuvidaOrThrowException(Integer id) {
		Optional<Postagem> optional = repository.findDuvidaById(id);
		if(! optional.isPresent())
			throw new RecursoNaoEncontradoException();
		
		return optional.get();
	}
	
	private Postagem createPostagem(DuvidaRequestDTO duvida, Aluno criador, Tutoria tutoria) {
		String descricao = duvida.getDescricao();
		String titulo = duvida.getTitulo();

		Postagem postagem = new Postagem(titulo, descricao);
		postagem.setCriador(criador);
		postagem.setTutoria(tutoria);
		return postagem;
	}

	private Tutoria findTutoriaDaDuvida(DuvidaRequestDTO duvida) {
		String nomeUsualDaDisciplina = duvida.getDisciplinaUsual();
		Tutoria tutoria = tutoriaRepository.findByDisciplinaNomeUsual(nomeUsualDaDisciplina);
		return tutoria;
	}

	private Aluno findAlunoIfExists(Integer idDoAluno) {
		boolean existe = alunoRepository.existsById(idDoAluno);
		return existe ? new Aluno(idDoAluno) : null;
	}
}
