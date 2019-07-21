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

import br.edu.ifrn.scatalapi.exception.TutoriaComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;
import br.edu.ifrn.scatalapi.repository.TutoriaRepository;

@RestController
@RequestMapping(value = "/tutoria/{id}/duvida", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class TutoriaDuvidaController {

	@Autowired
	private TutoriaRepository tutoriaRepository;
	
	@Autowired
	private PostagemRepository duvidaRepository;
	
	@GetMapping
	public ResponseEntity<Page<DuvidaResponseDTO>> findDuvidasById(@PathVariable Integer id,
			@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao) {
		if(! tutoriaRepository.existsById(id))
			throw new TutoriaComIdNaoEncontradoException();
		
		Page<Postagem> duvidas = duvidaRepository.findDuvidasByTutoriaId(paginacao, id);
		if (duvidas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(duvidas.map(DuvidaResponseDTO::new));
	}

}
