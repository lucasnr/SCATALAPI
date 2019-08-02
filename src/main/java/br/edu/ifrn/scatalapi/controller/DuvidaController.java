package br.edu.ifrn.scatalapi.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.dto.DuvidaUpdateDTO;
import br.edu.ifrn.scatalapi.exception.DuvidaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.swaggerutil.ApiPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/duvida", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"duvida"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com as dúvidas")
public class DuvidaController {

	@Autowired
	private PostagemRepository repository;

	@GetMapping("/{id}")
	@ApiOperation(value = "Busca uma dúvida por seu ID", response = DuvidaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera com sucesso a dúvida"), 
			@ApiResponse(code = 404, message = "Não existe dúvida com o ID informado")})
	public DuvidaResponseDTO findById(@ApiParam(required = true, name = "id", value = "O ID da dúvida a ser buscada") @PathVariable Integer id) {
		Postagem postagem = findDuvidaOrThrowException(id);
		return new DuvidaResponseDTO(postagem);
	}
	
	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ApiOperation(value = "Atualiza o titulo e descrição de uma dúvida por seu ID", response = DuvidaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Atualiza com sucesso o titulo e descrição da dúvida"), 
			@ApiResponse(code = 404, message = "Não existe dúvida com o ID informado")})
	public DuvidaResponseDTO updateById(@ApiParam(required = true, name = "id", value = "O ID da dúvida a ser atualizada") @PathVariable Integer id, 
			@RequestBody @Valid DuvidaUpdateDTO duvida){
		Postagem postagem = findDuvidaOrThrowException(id);
		postagem.setTitulo(duvida.getTitulo());
		postagem.setDescricao(duvida.getDescricao());
		return new DuvidaResponseDTO(postagem);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Deleta uma dúvida por seu ID", response = DuvidaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Deleta com sucesso a dúvida"), 
			@ApiResponse(code = 404, message = "Não existe dúvida com o ID informado")})
	public ResponseEntity<?> deleteById(@ApiParam(required = true, name = "id", value = "O ID da dúvida a ser deletada") @PathVariable Integer id){
		Postagem duvida = findDuvidaOrThrowException(id);
		repository.delete(duvida);			
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/search/{query}")
	@ApiOperation(value = "Busca dúvidas a partir de algum conteúdo", response = DuvidaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Encontrou com sucesso uma ou mais dúvidas com o conteúdo buscado"), 
			@ApiResponse(code = 204, message = "Não foram encontradas nenhuma dúvida com o conteúdo buscado")})
	@ApiPageable
	public ResponseEntity<Page<DuvidaResponseDTO>> findBySearch(@ApiParam(required = true, name = "query", value = "O conteúdo a ser buscado") @PathVariable String query, 
			@ApiIgnore @PageableDefault(page = 0, size = 10, sort = "registro", direction = Direction.DESC) Pageable paginacao){
		Page<Postagem> duvidas = repository.findAnyDuvidas(paginacao, query);
		if (duvidas.isEmpty()) 
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}
	
	private Postagem findDuvidaOrThrowException(Integer id) {
		return repository.findDuvidaById(id).orElseThrow(DuvidaComIdNaoEncontradaException::new);
	}
}
