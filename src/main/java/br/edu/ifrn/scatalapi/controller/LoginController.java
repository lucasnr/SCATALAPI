package br.edu.ifrn.scatalapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Credenciais;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.suapi.model.AlunoSUAP;

@RestController
@RequestMapping("/login")
public class LoginController {

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Token post(@RequestBody Credenciais credenciais) {
		Token token = new Token(credenciais);

		if (token.isValido()) {
			AlunoSUAP alunoSUAP = token.getUsuario(AlunoSUAP.class);
			String matricula = alunoSUAP.getMatricula();

			salvaAlunoCasoNaoExista(alunoSUAP, matricula);
		}

		return token;
	}

	@Autowired
	private AlunoRepository alunoRepository;
	
	private boolean salvaAlunoCasoNaoExista(AlunoSUAP alunoSUAP, String matricula) {
		Aluno aluno = alunoRepository.findByMatricula(matricula);
		boolean usuarioNaoExiste = aluno == null;

		boolean salvouUsuario = false;
		if (usuarioNaoExiste) {
			aluno = new Aluno(alunoSUAP);
			salvouUsuario = alunoRepository.save(aluno) != null;
		}

		return salvouUsuario;
	}
}
