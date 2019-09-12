package br.edu.ifrn.scatalapi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.edu.ifrn.scatalapi.dto.AlunoComFotoResponseDTO;
import br.edu.ifrn.scatalapi.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.exception.AlunoComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.AlunoComMatriculaNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.services.storage.Imagem;
import br.edu.ifrn.scatalapi.services.storage.StorageService;
import br.edu.ifrn.scatalapi.swaggerutil.ApiPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/aluno", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"aluno"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com os alunos")
public class AlunoController {

	@Autowired
	private AlunoRepository repository;

	@GetMapping
	@Cacheable(value = "alunos")
	@ApiOperation(value = "Busca todos os alunos", response = Page.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera os alunos com sucesso"), 
			@ApiResponse(code = 204, message = "Não existem alunos cadastrados")})
	@ApiPageable
	public ResponseEntity<Page<AlunoResponseDTO>> findAll(
			@ApiIgnore @PageableDefault(page = 0, size = 10, sort = "registro", direction = Direction.DESC) Pageable paginacao) {

		Page<Aluno> alunos = repository.findAll(paginacao);
		if (alunos.isEmpty())
			return ResponseEntity.noContent().build();

		return ResponseEntity.ok().body(alunos.map(AlunoResponseDTO::new));
	}

	@GetMapping("/{id}")
	@Cacheable(value = "aluno")
	@ApiOperation(value = "Busca um aluno por seu ID", response = AlunoResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera o aluno com sucesso"),
			@ApiResponse(code = 404, message = "Não existe aluno com o ID informado") 
	})
	public AlunoResponseDTO findById(@ApiParam(required = true, name = "id", value = "O ID do aluno a ser buscado") @PathVariable Integer id) {
		Aluno aluno = findByIdOrThrowException(id);
		return new AlunoResponseDTO(aluno);
	}

	@GetMapping("/matricula/{matricula}")
	@Cacheable(value = "alunoByMatricula")
	@ApiOperation(value = "Busca um aluno por sua matrícula", response = AlunoResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera o aluno com sucesso"),
			@ApiResponse(code = 404, message = "Não existe aluno com a matrícula informada") 
	})
	public AlunoResponseDTO findByMatricula(@ApiParam(required = true, name = "matricula", value = "A matrícula do aluno a ser buscado") @PathVariable String matricula) {
		Aluno aluno = repository.findByMatricula(matricula).orElseThrow(AlunoComMatriculaNaoEncontradoException::new);
		return new AlunoResponseDTO(aluno);
	}
	
	@Autowired
	private StorageService storageService;
	
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	@CacheEvict(allEntries = true, value = {"alunoByMatricula", "aluno", "alunos"})
	public AlunoComFotoResponseDTO updateFoto(@RequestParam("file") MultipartFile file, @PathVariable("id") Integer id, HttpServletRequest req) throws IOException {
		Aluno aluno = findByIdOrThrowException(id);
		String url = storageService.store(Imagem.from(file));
		aluno.setUrlFoto(url);
		return new AlunoComFotoResponseDTO(aluno);
	}

	private Aluno findByIdOrThrowException(Integer id) {
		return repository.findById(id).orElseThrow(AlunoComIdNaoEncontradoException::new);
	}
}