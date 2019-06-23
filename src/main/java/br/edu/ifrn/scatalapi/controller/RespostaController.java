package br.edu.ifrn.scatalapi.controller;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.exception.RecursoNaoEncontradoException;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.dto.RespostaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.RespostaUpdateDTO;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;

@RestController
@RequestMapping("/resposta")
public class RespostaController {

	@Autowired
	private PostagemRepository repository;
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RespostaResponseDTO findById(@PathVariable Integer id) {
		Postagem resposta = getRespostaOrThrowException(id);
		return new RespostaResponseDTO(resposta);
	}
	
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<RespostaResponseDTO> updateById(@PathVariable Integer id, @RequestBody RespostaUpdateDTO respostaDTO){
		Postagem resposta = getRespostaOrThrowException(id);
		resposta.setDescricao(respostaDTO.getDescricao());
		return ResponseEntity.ok(new RespostaResponseDTO(resposta));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		try {
			repository.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			throw new RecursoNaoEncontradoException();
		}
	}
	
	private Postagem getRespostaOrThrowException(Integer id) {
		Optional<Postagem> resposta = repository.findRespostaById(id);
		if (! resposta.isPresent())
			throw new RecursoNaoEncontradoException();
		return resposta.get();
	}
}




