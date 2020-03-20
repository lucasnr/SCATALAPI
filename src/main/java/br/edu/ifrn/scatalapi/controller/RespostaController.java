package br.edu.ifrn.scatalapi.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.edu.ifrn.scatalapi.dto.RespostaResponseDTO;
import br.edu.ifrn.scatalapi.dto.RespostaUpdateDTO;
import br.edu.ifrn.scatalapi.exception.RespostaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/resposta", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
@Api(tags = {"resposta"}, produces = MediaType.APPLICATION_JSON_VALUE, description = "Operações com as respostas")
public class RespostaController {

	@Autowired
	private PostagemRepository repository;
	
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Busca uma resposta por seu ID", response = DuvidaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Recupera com sucesso a resposta"),
			@ApiResponse(code = 404, message = "Não existe resposta com o ID informado")})
	public RespostaResponseDTO findById(@ApiParam(required = true, name = "id", value = "O ID da resposta") @PathVariable Integer id) {
		Postagem resposta = findRespostaOrThrowException(id);
		return new RespostaResponseDTO(resposta);
	}
	
	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ApiOperation(value = "Atualiza a descrição de uma resposta por seu ID", response = DuvidaResponseDTO.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Atualiza com sucesso a descrição da resposta"),
			@ApiResponse(code = 404, message = "Não existe resposta com o ID informado")})
	public RespostaResponseDTO updateById(@ApiParam(required = true, name = "id", value = "O ID da resposta") @PathVariable Integer id, 
			@RequestBody @Valid RespostaUpdateDTO respostaDTO){
		Postagem resposta = findRespostaOrThrowException(id);
		resposta.setDescricao(respostaDTO.getDescricao());
		return new RespostaResponseDTO(resposta);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Deleta uma resposta por seu ID")
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Deleta com sucesso a resposta"), 
			@ApiResponse(code = 404, message = "Não existe resposta com o ID informado")})
	public ResponseEntity<?> deleteById(@ApiParam(required = true, name = "id", value = "O ID da resposta") @PathVariable Integer id){
		Postagem resposta = findRespostaOrThrowException(id);
		repository.delete(resposta);
		return ResponseEntity.noContent().build();
	}
	
	private Postagem findRespostaOrThrowException(Integer id) throws RespostaComIdNaoEncontradaException {
		return repository.findRespostaById(id).orElseThrow(RespostaComIdNaoEncontradaException::new);
	}
}
