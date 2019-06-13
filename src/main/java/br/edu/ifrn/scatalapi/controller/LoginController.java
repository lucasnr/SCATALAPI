package br.edu.ifrn.scatalapi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Credenciais;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.suapi.model.AlunoSUAP;

@RestController
public class LoginController {

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Token post(@RequestBody Credenciais credenciais) {
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
