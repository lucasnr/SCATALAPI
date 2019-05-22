package br.edu.ifrn.scatalapi.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Credenciais;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.suapi.model.AlunoSUAP;

@Path("/login")
public class AutenticacaoService implements Service {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Token post(Credenciais credenciais) {
		Token token = new Token(credenciais);

		if (token.isValido()) {
			AlunoSUAP alunoSUAP = token.getUsuario(AlunoSUAP.class);
			String matricula = alunoSUAP.getMatricula();

			salvaAlunoCasoNaoExista(alunoSUAP, matricula);
		}

		return token;
	}

	private boolean salvaAlunoCasoNaoExista(AlunoSUAP alunoSUAP, String matricula) {
		AlunoDAO dao = DAOFactory.getAlunoDAO();
		Aluno aluno = dao.buscaPorMatricula(matricula);
		boolean usuarioNaoExiste = aluno == null;
		
		boolean salvouUsuario = false;
		if (usuarioNaoExiste) {
			aluno = new Aluno(alunoSUAP);
			salvouUsuario = dao.salvar(aluno);
		}
		
		dao.close();
		return salvouUsuario;
	}
}
