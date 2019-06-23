package br.edu.ifrn.scatalapi.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.edu.ifrn.scatalapi.exception.RecursoNaoEncontradoException;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.DuvidaRequestDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaUpdateDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping("/duvida")
public class DuvidaController {

	@Autowired
	private PostagemRepository repository;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DuvidaResponseDTO findById(@PathVariable Integer id) {
		Optional<Postagem> optional = repository.findById(id);
		if (! optional.isPresent())
			throw new RecursoNaoEncontradoException();

		Postagem postagem = optional.get();
		return new DuvidaResponseDTO(postagem);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<DuvidaResponseDTO> postDuvida(@RequestBody DuvidaRequestDTO duvida, UriComponentsBuilder uriBuilder) {
		Aluno aluno = getAlunoIfExists(duvida);
		// if(aluno == null)
		// throw exception
		Tutoria tutoria = getTutoriaDaDuvida(duvida);
		Postagem postagem = createPostagem(duvida, aluno, tutoria);

		ResponseEntity<DuvidaResponseDTO> resposta;
		boolean salvou = repository.save(postagem) != null;
		if (salvou) {
			DuvidaResponseDTO duvidaDTO = new DuvidaResponseDTO(postagem);
			URI location = uriBuilder.path("/duvida/{id}")
					.buildAndExpand(duvidaDTO.getId()).toUri();
			resposta = ResponseEntity.created(location).body(duvidaDTO);
		} else
			resposta = ResponseEntity.status(500).build();

		return resposta;
	}
	
	@PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<DuvidaResponseDTO> updateDuvida(@RequestBody DuvidaUpdateDTO duvida, @PathVariable Integer id){
		Optional<Postagem> optional = repository.findById(id);
		if(! optional.isPresent())
			throw new RecursoNaoEncontradoException();
		
		Postagem postagem = optional.get();
		postagem.setTitulo(duvida.getTitulo());
		postagem.setDescricao(duvida.getDescricao());
		return ResponseEntity.ok(new DuvidaResponseDTO(postagem));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteDuvida(@PathVariable Integer id){
		try {
			repository.deleteById(id);			
			return ResponseEntity.ok().build();
		} catch (Exception e) {			
			throw new RecursoNaoEncontradoException();
		}
	}
	
	private Postagem createPostagem(DuvidaRequestDTO duvida, Aluno criador, Tutoria tutoria) {
		String descricao = duvida.getDescricao();
		String titulo = duvida.getTitulo();

		Postagem postagem = new Postagem(titulo, descricao);
		postagem.setCriador(criador);
		postagem.setTutoria(tutoria);

		return postagem;
	}

	@Autowired
	private TutoriaRepository tutoriaRepository;

	private Tutoria getTutoriaDaDuvida(DuvidaRequestDTO duvida) {
		String nomeUsualDaDisciplina = duvida.getDisciplinaUsual();
		Tutoria tutoria = tutoriaRepository.findByDisciplinaNomeUsual(nomeUsualDaDisciplina);
		return tutoria;
	}

	@Autowired
	private AlunoRepository alunoRepository;

	private Aluno getAlunoIfExists(DuvidaRequestDTO duvida) {
		Integer id = duvida.getIdDoAluno();

		boolean existe = alunoRepository.existsById(id);
		return existe ? new Aluno(id) : null;
	}
}
