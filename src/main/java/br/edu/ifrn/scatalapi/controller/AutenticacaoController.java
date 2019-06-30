package br.edu.ifrn.scatalapi.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.dto.CredenciaisDTO;
import br.edu.ifrn.scatalapi.model.dto.TokenDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;
import br.edu.ifrn.suapi.model.AlunoSUAP;

@RestController
@RequestMapping("/auth") //mudar pra auth
public class AutenticacaoController {

	@Autowired
	private AlunoRepository alunoRepository;
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenDTO> authenticate(@RequestBody @Valid CredenciaisDTO credenciais, UriComponentsBuilder uriBuilder) throws FalhaAoConectarComSUAPException {
		ResponseEntity<TokenDTO> resposta;

		TokenDTO token = new TokenDTO(credenciais);
		if (token.isValido()) {
			Aluno alunoCriado = saveAlunoIfNotExists(token.asAluno());
			if(alunoCriado != null) {
				URI location = uriBuilder.path("/aluno/{id}").buildAndExpand(alunoCriado.getId()).toUri();				
				resposta = ResponseEntity.created(location).body(token);
			} else
				resposta = ResponseEntity.ok(token);
		} else 
			resposta = ResponseEntity.badRequest().body(token);
		
		return resposta;
	}
	
	private Aluno saveAlunoIfNotExists(AlunoSUAP alunoSUAP) {
		Optional<Aluno> optional = alunoRepository.findByMatricula(alunoSUAP.getMatricula());
		if (optional.isPresent())
			return null;

		Aluno aluno = new Aluno(alunoSUAP);
		alunoRepository.save(aluno);
		return aluno;
	}
}
