package br.edu.ifrn.scatalapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;
import br.edu.ifrn.scatalapi.util.ApiPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/tutoria/{id}/duvida", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"tutoria-duvida"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com as dúvidas de uma tutoria")
public class TutoriaDuvidaController {

	@Autowired
	private TutoriaRepository tutoriaRepository;
	
	@Autowired
	private PostagemRepository duvidaRepository;
	
	@GetMapping
	@ApiOperation(value = "Busca as dúvidas da tutoria por seu id", response = Page.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera as dúvidas da tutoria com sucesso"),
			@ApiResponse(code = 201, message = "A tutoria não possui nenhuma dúvida"),
			@ApiResponse(code = 404, message = "Não existe tutoria com o ID informado")})
	@ApiPageable
	public ResponseEntity<Page<DuvidaResponseDTO>> findDuvidasById(@ApiParam(required = true, name = "id", value = "O ID da tutoria") @PathVariable Integer id,
			@ApiIgnore @PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao) {
		if(! tutoriaRepository.existsById(id))
			throw new TutoriaComIdNaoEncontradoException();
		
		Page<Postagem> duvidas = duvidaRepository.findDuvidasByTutoriaId(paginacao, id);
		if (duvidas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}

}
