package br.edu.ifrn.scatalapi.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.dto.CredenciaisDTO;
import br.edu.ifrn.scatalapi.dto.TokenDTO;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Token;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.CursoRepository;
import br.edu.ifrn.scatalapi.repository.EmailRepository;
import br.edu.ifrn.suapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;
import br.edu.ifrn.suapi.exception.TokenInvalidoException;
import br.edu.ifrn.suapi.model.AlunoSUAP;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/aluno", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"login"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com o login dos alunos")
public class LoginController {
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private EmailRepository emailRepository;

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ApiOperation(value = "Faz login de um aluno usando suas credenciais com o SUAP e retorna o seu token", response = AlunoResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera o token do aluno com sucesso"),
			@ApiResponse(code = 400, message = "As credenciais informadas não são válidas") 
	})
	public TokenDTO login(@RequestBody @Valid CredenciaisDTO credenciais) throws FalhaAoConectarComSUAPException, CredenciaisIncorretasException {
		Token token = new Token(credenciais);
		saveAlunoIfNotExists(token.asAluno());
		return new TokenDTO(token.getToken());
	}
	
	@GetMapping("/meus-dados")
	@ApiOperation(value = "Retorna os dados do aluno a partir do Header 'Token'", response = AlunoResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera os dados do aluno com sucesso"),
			@ApiResponse(code = 400, message = "O token não foi informado ou não é válido") 
	})
	public AlunoResponseDTO findByToken(@ApiIgnore @RequestHeader(required = true, value = "Token") String tokenHeader) throws FalhaAoConectarComSUAPException, TokenInvalidoException {
		Token token = new Token(tokenHeader);
		return new AlunoResponseDTO(alunoRepository.findByMatricula(token.asAluno().getMatricula()).get());
	}
	
	private void saveAlunoIfNotExists(AlunoSUAP aluno) {
		boolean existe = alunoRepository.findByMatricula(aluno.getMatricula()).isPresent();
		if(existe)
			return;
		
		Aluno toSave = new Aluno(aluno);
		toSave.setCurso(cursoRepository.findByCodigoSUAP(toSave.getCurso().getCodigoSUAP()));
		Aluno savedAluno = alunoRepository.save(toSave);
		saveAllEmails(savedAluno);
	}

	private void saveAllEmails(Aluno aluno) {
		aluno.getEmails().forEach(emailRepository::save);
	}
}
