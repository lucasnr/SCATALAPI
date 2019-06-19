package br.edu.ifrn.scatalapi.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.dao.PostagemDAO;
import br.edu.ifrn.scatalapi.dao.TutoriaDAO;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.DuvidaDTO;

@RestController
@RequestMapping("/tutoria")
public class TutoriaController {

	@PostMapping(value = "/{disciplina}/duvida", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postarNovaDuvida(@PathVariable String disciplina, @RequestBody DuvidaDTO duvida) throws URISyntaxException {
		System.out.println(duvida);
		Aluno aluno = pegaAlunoDaDuvida(duvida);
		Tutoria tutoria = pegaTutoriaDaDuvida(disciplina);
		Postagem postagem = criaPostagem(duvida, aluno, tutoria);

		boolean salvou = salvaPostagem(postagem);
		return salvou ? ResponseEntity.created(new URI("localhost:8080/duvida/" + postagem.getId())).build() : ResponseEntity.status(500).build();
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

//	@DELETE
//	@Path("/{disciplina}/duvida/{id}")
//	public Response removeDuvida(@PathParam("nome") String nomeDaDisciplina, @PathParam("id") Integer idDaDuvida) {
//
//		return null;
//	}
}
