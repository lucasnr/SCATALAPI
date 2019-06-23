package br.edu.ifrn.scatalapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.NaoAutorizadoTokenInvalidoException;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.scatalapi.model.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
	
	@Autowired
	private AlunoRepository repository;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AlunoResponseDTO> finddAll() {
		List<Aluno> all = repository.findAll();
		List<AlunoResponseDTO> alunos = all.stream().map(AlunoResponseDTO::new).collect(Collectors.toList());
		return alunos;
	}
	

	@GetMapping(value = "/{matricula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AlunoResponseDTO findByMatricula(@RequestHeader("token") String headerToken, @PathVariable String matricula) {
		
		Token token = new Token(headerToken);
		if (!token.isValido()) {
			throw new NaoAutorizadoTokenInvalidoException();
		}

		Aluno aluno = repository.findByMatricula(matricula);
		return new AlunoResponseDTO(aluno);
	}
}
