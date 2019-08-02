package br.edu.ifrn.scatalapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dto.TutoriaDetalhadaResponseDTO;
import br.edu.ifrn.scatalapi.dto.TutoriaResponseDTO;
import br.edu.ifrn.scatalapi.dto.TutoriaUpdateDTO;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Disciplina;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/tutoria", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"tutoria"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com as tutorias")
public class TutoriaController {

	@Autowired
	private TutoriaRepository repository;

	@GetMapping
	@Cacheable(value = "tutorias")
	@ApiOperation(value = "Busca todos as tutorias", response = List.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Recupera as tutorias com sucesso")}) 
	public List<TutoriaResponseDTO> findAll() {
		return repository.findAll().stream().map(TutoriaResponseDTO::new).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@Cacheable(value = "tutoria")
	@ApiOperation(value = "Busca uma tutoria por seu ID", response = TutoriaDetalhadaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera a tutoria com sucesso"), 
			@ApiResponse(code = 404, message = "Não existe tutoria com o ID informado")})
	public TutoriaDetalhadaResponseDTO findById(@ApiParam(required = true, name = "id", value = "O ID da tutoria") @PathVariable Integer id) {
		Tutoria tutoria = findTutoriaOrThrowException(id);
		return new TutoriaDetalhadaResponseDTO(tutoria);
	}
	
	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@CacheEvict(value = {"tutorias", "tutoria"}, allEntries = true)
	@ApiOperation(value = "Atualiza o nome e nome usual da tutoria por seu ID", response = TutoriaDetalhadaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Atualiza o nome e nome usual da tutoria com sucesso"), 
			@ApiResponse(code = 400, message = "Os dados informados não são válidos"), 
			@ApiResponse(code = 404, message = "Não existe tutoria com o ID informado")})
	public TutoriaDetalhadaResponseDTO uptadeById(@ApiParam(required = true, name = "id", value = "O ID da tutoria") @PathVariable Integer id, 
			@RequestBody @Valid TutoriaUpdateDTO tutoriaDTO) {
		Tutoria tutoria = findTutoriaOrThrowException(id);
		Disciplina disciplina = tutoria.getDisciplina();
		disciplina.setNome(tutoriaDTO.getNome());
		disciplina.setNomeUsual(tutoriaDTO.getNomeUsual());
		return new TutoriaDetalhadaResponseDTO(tutoria);
	}

	private Tutoria findTutoriaOrThrowException(Integer id) throws TutoriaComIdNaoEncontradoException {
		return repository.findById(id).orElseThrow(TutoriaComIdNaoEncontradoException::new);
	}
}
