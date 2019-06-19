package br.edu.ifrn.scatalapi.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.dao.PostagemDAO;
import br.edu.ifrn.scatalapi.dao.TutoriaDAO;
import br.edu.ifrn.scatalapi.exception.DuvidaNaoEncontradaParaTutoria;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.DuvidaDTO;

@RestController
@RequestMapping("/tutoria/{disciplina}/duvida")
public class DuvidaController {
	
	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public DuvidaDTO get(@PathVariable Integer id, @PathVariable String disciplina) {
		PostagemDAO dao = DAOFactory.getPostagemDAO();
		Postagem postagem = dao.buscaPorId(id);
		boolean isDaTutoria = postagem.getTutoria().getDisciplina().getNomeUsual().equalsIgnoreCase(disciplina);
		dao.close();
		if(! isDaTutoria) 
			throw new DuvidaNaoEncontradaParaTutoria();
		
		DuvidaDTO duvida = new DuvidaDTO(postagem);
		return duvida;
	}
	

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postarNovaDuvida(@PathVariable String disciplina, @RequestBody DuvidaDTO duvida) throws URISyntaxException {
		Aluno aluno = pegaAlunoDaDuvida(duvida);
		Tutoria tutoria = pegaTutoriaDaDuvida(disciplina);
		Postagem postagem = criaPostagem(duvida, aluno, tutoria);

		ResponseEntity<?> resposta;
		boolean salvou = salvaPostagem(postagem);
		if (salvou) {		
			StringBuilder caminho = new StringBuilder();
			caminho.append("localhost:8080/tutoria/");
			caminho.append(disciplina);
			caminho.append("/duvida/");
			caminho.append(postagem.getId());
			URI location = new URI(caminho.toString());
			resposta = ResponseEntity.created(location).build();
		} else 
			resposta = ResponseEntity.status(500).build();
		
		return resposta;
	}

	private boolean salvaPostagem(Postagem postagem) {
		PostagemDAO postagemDAO = DAOFactory.getPostagemDAO();
		boolean salvou = postagemDAO.salvar(postagem);
		postagemDAO.close();
		return salvou;
	}

	private Postagem criaPostagem(DuvidaDTO duvida, Aluno criador, Tutoria tutoria) {
		String descricao = duvida.getDescricao();
		String titulo = duvida.getTitulo();

		Postagem postagem = new Postagem(titulo, descricao);
		postagem.setCriador(criador);
		postagem.setTutoria(tutoria);

		return postagem;
	}

	private Tutoria pegaTutoriaDaDuvida(String nomeUsualDaDisciplina) {
		TutoriaDAO tutoriaDAO = DAOFactory.getTutoriaDAO();
		Tutoria tutoria = tutoriaDAO.buscaPorNomeUsualDaDisciplina(nomeUsualDaDisciplina);
		tutoriaDAO.close();
		return tutoria;
	}

	private Aluno pegaAlunoDaDuvida(DuvidaDTO duvida) {
		Integer idDoAluno = duvida.getIdDoAluno();

		AlunoDAO alunoDAO = DAOFactory.getAlunoDAO();
		Aluno criador = alunoDAO.buscaPorId(idDoAluno);
		alunoDAO.close();
		return criador;
	}
}
