package br.edu.ifrn.scatalapi.controller;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifrn.scatalapi.exception.AlunoComIdNaoEncontradoException;
import br.edu.ifrn.scatalapi.exception.DuvidaComIdNaoEncontradaException;
import br.edu.ifrn.scatalapi.exception.FalhaAoSalvarNoBancoDeDadosException;
import br.edu.ifrn.scatalapi.interceptor.AutenticadoRequired;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.dto.RespostaRequestDTO;
import br.edu.ifrn.scatalapi.model.dto.RespostaResponseDTO;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.repository.PostagemRepository;

@RestController
@RequestMapping(value = "/duvida/{id}/resposta", produces = MediaType.APPLICATION_JSON_VALUE)
@AutenticadoRequired
public class DuvidaRespostaController {

	@Autowired
	private PostagemRepository postagemRepository;
	
	@Autowired
	private AlunoRepository alunoRepository;
	
	@GetMapping
	public ResponseEntity<Page<RespostaResponseDTO>> findRespostasById(@PathVariable Integer id,
			@PageableDefault(page=0, size=10, sort="registro", direction=Direction.DESC) Pageable paginacao) {
		Postagem postagem = findDuvidaOrThrowException(id);
		Page<Postagem> respostas = postagemRepository.findRespostasByDuvidaId(paginacao, postagem.getId());
		if(respostas.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(respostas.map(RespostaResponseDTO::new));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<RespostaResponseDTO> postResposta(@PathVariable Integer id, 
			@RequestBody @Valid RespostaRequestDTO resposta, UriComponentsBuilder uriBuilder){
		Postagem postagem = buildResposta(id, resposta);
		
		Postagem salvo = postagemRepository.save(postagem);
		if (salvo == null)
			throw new FalhaAoSalvarNoBancoDeDadosException();
		
		RespostaResponseDTO responseDTO = new RespostaResponseDTO(postagem);
		URI location = uriBuilder.path("/resposta/{id}").buildAndExpand(responseDTO.getId()).toUri();
		return ResponseEntity.created(location).body(responseDTO);
	}

	private Postagem buildResposta(Integer duvidaId, RespostaRequestDTO resposta) {
		Postagem duvida = findDuvidaOrThrowException(duvidaId);
		Aluno criador = findAlunoOrThrowException(resposta.getIdDoAluno());
		
		Postagem postagem = new Postagem(null, resposta.getDescricao());
		postagem.setCriador(criador);
		postagem.setPostagemPai(duvida);
		postagem.setTutoria(duvida.getTutoria());
		return postagem;
	}
	

	private Aluno findAlunoOrThrowException(Integer id) {
		return alunoRepository.findById(id).orElseThrow(AlunoComIdNaoEncontradoException::new);
	}

	private Postagem findDuvidaOrThrowException(Integer id) {
		return postagemRepository.findById(id).orElseThrow(DuvidaComIdNaoEncontradaException::new);
	}
}
