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

@RestController
@RequestMapping(value = "/duvida", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class DuvidaController {

	@Autowired
	private PostagemRepository repository;

	@GetMapping("/{id}")
	public DuvidaResponseDTO findById(@PathVariable Integer id) {
		Postagem postagem = findDuvidaOrThrowException(id);
		return new DuvidaResponseDTO(postagem);
	}
	
	@PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<DuvidaResponseDTO> updateById(@PathVariable Integer id, 
			@RequestBody @Valid DuvidaUpdateDTO duvida){
		Postagem postagem = findDuvidaOrThrowException(id);
		postagem.setTitulo(duvida.getTitulo());
		postagem.setDescricao(duvida.getDescricao());
		return ResponseEntity.ok().body(new DuvidaResponseDTO(postagem));
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		try {
			repository.deleteById(id);			
			return ResponseEntity.noContent().build();
		} catch (Exception e) {			
			throw new DuvidaComIdNaoEncontradaException();
		}
	}
	
	@GetMapping("/search/{query}")
	public ResponseEntity<Page<DuvidaResponseDTO>> findBySearch(@PathVariable String query, 
			@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao){
		Page<Postagem> duvidas = repository.findAnyDuvidas(paginacao, query);
		if (duvidas.isEmpty()) 
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}
	
	private Postagem findDuvidaOrThrowException(Integer id) {
		return repository.findDuvidaById(id).orElseThrow(DuvidaComIdNaoEncontradaException::new);
	}
}
