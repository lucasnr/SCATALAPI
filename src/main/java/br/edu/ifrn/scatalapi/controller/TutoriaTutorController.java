package br.edu.ifrn.scatalapi.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.dto.TutoriaDetalhadaResponseDTO;
import br.edu.ifrn.scatalapi.dto.TutoriaTutoresUpdateDTO;
import br.edu.ifrn.scatalapi.exception.AlunoComMatriculaNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Tutoria;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/tutoria/{id}/tutor", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"tutoria-tutor"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com os tutores de uma tutoria")
public class TutoriaTutorController {

	@Autowired
	private TutoriaRepository tutoriaRepository;
	
	@Autowired
	private AlunoRepository alunoRepository;

	@GetMapping
	@Cacheable(value = "tutores")
	@ApiOperation(value = "Busca os tutores da tutoria por seu ID", response = List.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera os tutores da tutoria com sucesso"), 
			@ApiResponse(code = 201, message = "A tutoria não possui nenhum tutor"),
			@ApiResponse(code = 404, message = "Não existe tutoria com o ID informado")})
	public ResponseEntity<List<AlunoResponseDTO>> findTutoresById(@ApiParam(required = true, name = "id", value = "O ID da tutoria") @PathVariable Integer id) {
		Tutoria tutoria = findTutoriaOrThrowException(id);
		List<Aluno> tutores = tutoria.getTutores();
		if (tutores.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(tutores.stream().map(AlunoResponseDTO::new).collect(Collectors.toList()));
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@CacheEvict(value= {"tutorias", "tutoria", "tutores"}, allEntries=true)
	@ApiOperation(value = "Atualiza os tutores da tutoria por seu ID", response = TutoriaDetalhadaResponseDTO.class, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Atualiza os tutores da tutoria com sucesso"), 
			@ApiResponse(code = 400, message = "Os dados informados não são válidos"),
			@ApiResponse(code = 404, message = "Não existe tutoria com o ID informado")})
	public ResponseEntity<TutoriaDetalhadaResponseDTO> updateTutoresById(@ApiParam(required = true, name = "id", value = "O ID da tutoria") @PathVariable Integer id,
			@RequestBody @Valid TutoriaTutoresUpdateDTO tutoriaDTO) {
		
		Tutoria tutoria = findTutoriaOrThrowException(id);
		try {
			List<Aluno> tutores = tutoriaDTO.getTutores().stream()
					.map(matricula -> alunoRepository.findByMatricula(matricula.getMatricula()))
					.map(Optional::get)
					.collect(Collectors.toList());

			tutoria.setTutores(tutores);
			return ResponseEntity.ok(new TutoriaDetalhadaResponseDTO(tutoria));
		} catch (Exception e) {
			throw new AlunoComMatriculaNaoEncontradoException();
		}
	}
	
	private Tutoria findTutoriaOrThrowException(Integer id) throws TutoriaComIdNaoEncontradoException {
		return tutoriaRepository.findById(id).orElseThrow(TutoriaComIdNaoEncontradoException::new);
	}
}
