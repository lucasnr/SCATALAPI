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

import br.edu.ifrn.scatalapi.dto.DuvidaRequestDTO;
import br.edu.ifrn.scatalapi.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.exception.AlunoComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.FalhaAoSalvarNoBancoDeDadosException;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/aluno/{id}/duvida", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"aluno-duvida"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Opera��es com as duvidas de um aluno")
@ApiResponses(@ApiResponse(code = 401, message = "Voc� n�o tem permiss�o para acessar esse recurso ou n�o informou o token de Autoriza��o"))
public class AlunoDuvidaController {

	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private PostagemRepository duvidaRepository;

	@Autowired
	private TutoriaRepository tutoriaRepository;
	
	@GetMapping
	@ApiOperation(value = "Busca as d�vidas de um aluno por seu ID")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "O aluno n�o possui nenhuma d�vida"), 
			@ApiResponse(code = 404, message = "N�o existe aluno com o ID informado")
	})
	public ResponseEntity<Page<DuvidaResponseDTO>> findDuvidasById(@ApiParam(required = true, name = "id1", value = "O ID do aluno a ser buscado") @PathVariable Integer id,
			@PageableDefault(page = 0, size = 5, sort = "registro", direction = Direction.DESC) Pageable paginacao) {
		if (! alunoRepository.existsById(id))
			throw new AlunoComIdNaoEncontradoException();
		
		Page<Postagem> duvidas = duvidaRepository.findDuvidasByCriadorId(paginacao, id);
		if (duvidas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ApiOperation(value = "Cria uma d�vida para o aluno por seu ID")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "D�vida criada com sucesso"),
			@ApiResponse(code = 400, message = "Os dados informados no corpo da requisi��o n�o s�o v�lidos"),
			@ApiResponse(code = 404, message = "N�o existe aluno ou tutoria com os respectivos IDs informados"),
	})
	public ResponseEntity<DuvidaResponseDTO> post(@ApiParam(required = true, name = "id2", value = "O ID do aluno a ser buscado") @PathVariable Integer id,
			@RequestBody @Valid DuvidaRequestDTO duvida, UriComponentsBuilder uriBuilder) {
		
		Postagem postagem = buildDuvida(id, duvida);
		boolean salvou = duvidaRepository.save(postagem) != null;
		if (salvou) {
			DuvidaResponseDTO duvidaDTO = new DuvidaResponseDTO(postagem);
			URI location = uriBuilder.path("/duvida/{id}")
					.buildAndExpand(duvidaDTO.getId()).toUri();
			return ResponseEntity.created(location).body(duvidaDTO);
		} 
		
		throw new FalhaAoSalvarNoBancoDeDadosException();
	}

	private Postagem buildDuvida(Integer alunoId, DuvidaRequestDTO duvida) {
		Aluno criador = findAlunoOrThrowException(alunoId);
		Tutoria tutoria = findTutoriaOrThrowException(duvida.getIdDaTutoria());
		
		String descricao = duvida.getDescricao();
		String titulo = duvida.getTitulo();

		Postagem postagem = new Postagem(titulo, descricao);
		postagem.setCriador(criador);
		postagem.setTutoria(tutoria);
		return postagem;
	}

	private Tutoria findTutoriaOrThrowException(Integer idDaTutoria) throws TutoriaComIdNaoEncontradoException {
		boolean existe = tutoriaRepository.existsById(idDaTutoria);
		if(existe)
			return new Tutoria(idDaTutoria);
		
		throw new TutoriaComIdNaoEncontradoException();
	}

	private Aluno findAlunoOrThrowException(Integer idDoAluno) throws AlunoComIdNaoEncontradoException {
		boolean existe = alunoRepository.existsById(idDoAluno);
		if(existe)
			return new Aluno(idDoAluno);
			
		throw new AlunoComIdNaoEncontradoException();
	}
}
