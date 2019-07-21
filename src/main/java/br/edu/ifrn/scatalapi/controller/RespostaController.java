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

import br.edu.ifrn.scatalapi.exception.RespostaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.dto.RespostaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.RespostaUpdateDTO;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;

@RestController
@RequestMapping(value = "/resposta", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class RespostaController {

	@Autowired
	private PostagemRepository repository;
	
	@GetMapping(value = "/{id}")
	public RespostaResponseDTO findById(@PathVariable Integer id) {
		Postagem resposta = findRespostaOrThrowException(id);
		return new RespostaResponseDTO(resposta);
	}
	
	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<RespostaResponseDTO> updateById(@PathVariable Integer id, 
			@RequestBody @Valid RespostaUpdateDTO respostaDTO){
		Postagem resposta = findRespostaOrThrowException(id);
		resposta.setDescricao(respostaDTO.getDescricao());
		return ResponseEntity.ok(new RespostaResponseDTO(resposta));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		try {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			throw new RespostaComIdNaoEncontradaException();
		}
	}
	
	private Postagem findRespostaOrThrowException(Integer id) throws RespostaComIdNaoEncontradaException {
		return repository.findRespostaById(id).orElseThrow(RespostaComIdNaoEncontradaException::new);
	}
}
