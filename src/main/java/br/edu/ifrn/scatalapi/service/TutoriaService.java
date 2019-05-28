package br.edu.ifrn.scatalapi.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.dao.PostagemDAO;
import br.edu.ifrn.scatalapi.dao.TutoriaDAO;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.model.dto.DuvidaDTO;

@Path("/tutoria")
public class TutoriaService implements Service {

	@POST
	@Path("/{disciplina}/duvida/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postarNovaDuvida(DuvidaDTO duvida, @PathParam("disciplina") String nomeUsualDaDisciplina) {
		Aluno aluno = pegaAlunoDaDuvida(duvida);
		Tutoria tutoria = pegaTutoriaDaDuvida(nomeUsualDaDisciplina);
		Postagem postagem = criaPostagem(duvida, aluno, tutoria);
		
		boolean salvou = salvaPostagem(postagem);
		return salvou ? Response.ok("Sucesso").build() : Response.serverError().build();
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
	
	@DELETE
	@Path("/{disciplina}/duvida/{id}")
	public Response removeDuvida(@PathParam("nome") String nomeDaDisciplina, @PathParam("id") Integer idDaDuvida) {
		
		
		return null;
	}
}
