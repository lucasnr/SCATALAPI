package br.edu.ifrn.scatalapi.controller;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifrn.scatalapi.dto.RespostaRequestDTO;
import br.edu.ifrn.scatalapi.dto.RespostaResponseDTO;
import br.edu.ifrn.scatalapi.exception.AlunoComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.DuvidaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.exception.FalhaAoSalvarNoBancoDeDadosException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.swaggerutil.ApiPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/duvida/{id}/resposta", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"duvida-resposta"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com as respostas de uma dúvida")
public class DuvidaRespostaController {

	@Autowired
	private PostagemRepository postagemRepository;
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@GetMapping
	@ApiOperation(value = "Busca as respostas de uma dúvida por seu ID", response = Page.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera com sucesso as respostas da dúvida"),
			@ApiResponse(code = 204, message = "A dúvida não possui nenhuma resposta"), 
			@ApiResponse(code = 404, message = "Não existe dúvida com o ID informado")})
	@ApiPageable
	public ResponseEntity<Page<RespostaResponseDTO>> findRespostasById(@ApiParam(required = true, name = "id", value = "O ID da dúvida") @PathVariable Integer id,
			@ApiIgnore @PageableDefault(page = 0, size = 10, sort = "registro", direction = Direction.DESC) Pageable paginacao) {
		Postagem postagem = findDuvidaOrThrowException(id);
		Page<Postagem> respostas = postagemRepository.findRespostasByDuvidaId(paginacao, postagem.getId());
		if(respostas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(respostas.map(RespostaResponseDTO::new));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ApiOperation(value = "Cria uma nova resposta para uma dúvida por seu ID", response = RespostaResponseDTO.class, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "A resposta foi criada com sucesso"), 
			@ApiResponse(code = 400, message = "Os dados da resposta não são válidos"), 
			@ApiResponse(code = 404, message = "Não existe dúvida com o ID informado")})
	public ResponseEntity<RespostaResponseDTO> postResposta(@ApiParam(required = true, name = "id", value = "O ID da dúvida") @PathVariable Integer id, 
			@RequestBody @Valid RespostaRequestDTO resposta, UriComponentsBuilder uriBuilder){
		Postagem postagem = buildResposta(id, resposta);
		
		Postagem salvo = postagemRepository.save(postagem);
		if (salvo == null)
			throw new FalhaAoSalvarNoBancoDeDadosException();
		
		RespostaResponseDTO responseDTO = new RespostaResponseDTO(postagem);
		URI location = uriBuilder.path("/resposta/{id}").buildAndExpand(responseDTO.getId()).toUri();
		return ResponseEntity.created(location).body(responseDTO);
	}

	private Postagem buildResposta(Integer duvidaId, RespostaRequestDTO resposta) {
		Postagem duvida = findDuvidaOrThrowException(duvidaId);
		Aluno criador = findAlunoOrThrowException(resposta.getIdDoAluno());
		
		Postagem postagem = new Postagem(null, resposta.getDescricao());
		postagem.setCriador(criador);
		postagem.setPostagemPai(duvida);
		postagem.setTutoria(duvida.getTutoria());
		return postagem;
	}
	

	private Aluno findAlunoOrThrowException(Integer id) {
		return alunoRepository.findById(id).orElseThrow(AlunoComIdNaoEncontradoException::new);
	}

	private Postagem findDuvidaOrThrowException(Integer id) {
		return postagemRepository.findById(id).orElseThrow(DuvidaComIdNaoEncontradaException::new);
	}
}
