package br.edu.ifrn.scatalapi.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifrn.scatalapi.exception.DuvidaNaoEncontradaParaTutoria;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.DuvidaDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping("/tutoria/{disciplina}/duvida")
public class DuvidaController {

	@Autowired
	private PostagemRepository repository;
	
	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public DuvidaDTO get(@PathVariable Integer id, @PathVariable String disciplina) {
		Postagem postagem = repository.findById(id).get();
		boolean isDaTutoria = postagem.getTutoria().getDisciplina().getNomeUsual().equalsIgnoreCase(disciplina);
		if(! isDaTutoria) 
			throw new DuvidaNaoEncontradaParaTutoria();
		
		DuvidaDTO duvida = new DuvidaDTO(postagem);
		return duvida;
	}
	

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DuvidaDTO> postarNovaDuvida(@PathVariable String disciplina, @RequestBody DuvidaDTO duvida, UriComponentsBuilder uriBuilder) {
		Aluno aluno = pegaAlunoDaDuvida(duvida);
		Tutoria tutoria = pegaTutoriaDaDuvida(disciplina);
		Postagem postagem = criaPostagem(duvida, aluno, tutoria);

		ResponseEntity<DuvidaDTO> resposta;
		boolean salvou = salvaPostagem(postagem);
		if (salvou) {
			Integer id = postagem.getId();
			duvida.setId(id);
			URI location = uriBuilder.path("/tutoria/{disciplina}/duvida/{id}").buildAndExpand(disciplina, id).toUri();
			resposta = ResponseEntity.created(location).body(duvida);
		} else 
			resposta = ResponseEntity.status(500).build();
		
		return resposta;
	}

	private boolean salvaPostagem(Postagem postagem) {
		return repository.save(postagem) != null;
	}

	private Postagem criaPostagem(DuvidaDTO duvida, Aluno criador, Tutoria tutoria) {
		String descricao = duvida.getDescricao();
		String titulo = duvida.getTitulo();

		Postagem postagem = new Postagem(titulo, descricao);
		postagem.setCriador(criador);
		postagem.setTutoria(tutoria);

		return postagem;
	}


	@Autowired
	private TutoriaRepository tutoriaRepository;
	
	private Tutoria pegaTutoriaDaDuvida(String nomeUsualDaDisciplina) {
		Tutoria tutoria = tutoriaRepository.findByDisciplinaNomeUsual(nomeUsualDaDisciplina);
		return tutoria;
	}


	@Autowired
	private AlunoRepository alunoRepository;
	
	private Aluno pegaAlunoDaDuvida(DuvidaDTO duvida) {
		Integer idDoAluno = duvida.getIdDoAluno();

		Aluno criador = alunoRepository.findById(idDoAluno).get();
		return criador;
	}
}
