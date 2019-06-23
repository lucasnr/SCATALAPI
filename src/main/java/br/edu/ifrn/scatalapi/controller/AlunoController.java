package br.edu.ifrn.scatalapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public Page<AlunoResponseDTO> finddAll(@RequestParam Integer offset, @RequestParam Integer number) {
		Pageable paginacao = PageRequest.of(offset, number);
//		Pageable paginacao = PageRequest.of(offset, number, Direction.DESC, "registro");
		Page<Aluno> page = repository.findAll(paginacao);
		return page.map(AlunoResponseDTO::new);
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
