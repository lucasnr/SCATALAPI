package br.edu.ifrn.scatalapi.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.scatalapi.model.dto.Credenciais;
import br.edu.ifrn.suapi.model.AlunoSUAP;

@Path("/login")
public class AutenticacaoService implements Service{

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Token post(Credenciais credenciais) {
		Token token = new Token(credenciais);
		
		if (token.isValido()) {
			AlunoSUAP alunoSUAP = token.getUsuario(AlunoSUAP.class);
			Aluno aluno = new Aluno(alunoSUAP);
			AlunoDAO dao = new DAOFactory().getAlunoDAO();
			
			Aluno busca = dao.buscaPorMatricula(aluno.getMatricula());
			
		}
		
		return token;
	}
}
