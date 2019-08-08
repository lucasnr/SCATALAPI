//package br.edu.ifrn.scatalapi.controller;
//
//import java.net.URI;
//import java.util.Optional;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import br.edu.ifrn.scatalapi.dto.CredenciaisDTO;
//import br.edu.ifrn.scatalapi.dto.TokenDTO;
//import br.edu.ifrn.scatalapi.model.Aluno;
//import br.edu.ifrn.scatalapi.model.Token;
//import br.edu.ifrn.scatalapi.repository.AlunoRepository;
//import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;
//import br.edu.ifrn.suapi.model.AlunoSUAP;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//
//@RestController
//@RequestMapping(value="/auth", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//@Api(tags = {"auth"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações de autenticação")
//public class AutorizacaoController {
//
//	@Autowired
//	private AlunoRepository alunoRepository;
//
//	@PostMapping
//	@ApiOperation(value = "Obtem o token do SUAP para o aluno com as credenciais informadas", response = Token.class)
//	@ApiResponses(value = { 
//			@ApiResponse(code = 200, message = "Recupera com sucesso o token do aluno"), 
//			@ApiResponse(code = 201, message = "Recupera com sucesso o token do aluno e salva esse aluno no banco de dados"), 
//			@ApiResponse(code = 400, message = "As credenciais informadas não são válidas"),
//			@ApiResponse(code = 500, message = "Ocorreu um erro ao se comunicar com o SUAP")})
//	public ResponseEntity<Token> authenticate(@RequestBody @Valid CredenciaisDTO credenciais,
//			UriComponentsBuilder uriBuilder) throws FalhaAoConectarComSUAPException {
//		ResponseEntity<Token> resposta;
//
//		Token token = new Token(credenciais);
//		if (token.isValido()) {
//			Aluno alunoCriado = saveAlunoIfNotExists(token.asAluno());
//			if (alunoCriado != null) {
//				URI location = uriBuilder.path("/aluno/{id}").buildAndExpand(alunoCriado.getId()).toUri();
//				resposta = ResponseEntity.created(location).body(token);
//			} else
//				resposta = ResponseEntity.ok(token);
//		} else
//			resposta = ResponseEntity.badRequest().body(token);
//
//		return resposta;
//	}
//
//	@PostMapping(value = "/verify")
//	@ApiOperation(value = "Valida um token informado", response = Token.class)
//	@ApiResponses(value = { 
//			@ApiResponse(code = 200, message = "O token informado foi validado com sucesso"), 
//			@ApiResponse(code = 500, message = "Ocorreu um erro ao se comunicar com o SUAP")})
//	public ResponseEntity<Token> validate(@RequestBody TokenDTO token) throws FalhaAoConectarComSUAPException {
//		Token tokenDTO = new Token(token.getToken());
//		return ResponseEntity.ok(tokenDTO);
//	}
//
//	private Aluno saveAlunoIfNotExists(AlunoSUAP alunoSUAP) {
//		Optional<Aluno> optional = alunoRepository.findByMatricula(alunoSUAP.getMatricula());
//		if (optional.isPresent())
//			return null;
//
//		Aluno aluno = new Aluno(alunoSUAP);
//		alunoRepository.save(aluno);
//		return aluno;
//	}
//}
