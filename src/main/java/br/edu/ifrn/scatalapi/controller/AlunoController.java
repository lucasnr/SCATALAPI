package br.edu.ifrn.scatalapi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.scatalapi.model.dto.AlunoDTO;

@RestController
public class AlunoController {

	@GetMapping(value = "/aluno/{matricula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AlunoDTO get(@RequestHeader("token") String tokenConteudo, @PathVariable String matricula) {
		Token tokenObject = new Token(tokenConteudo);
		if (!tokenObject.isValido()) {
			return null;
		}

		AlunoDAO dao = DAOFactory.getAlunoDAO();
		Aluno aluno = dao.buscaPorMatricula(matricula);
		dao.close();
		return new AlunoDTO(aluno);
	}

}
