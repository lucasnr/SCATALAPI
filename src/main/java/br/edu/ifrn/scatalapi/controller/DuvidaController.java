package br.edu.ifrn.scatalapi.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.dao.PostagemDAO;
import br.edu.ifrn.scatalapi.model.Postagem;
import br.edu.ifrn.scatalapi.model.dto.DuvidaDTO;

@RestController
@RequestMapping("/duvida")
public class DuvidaController {

	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public DuvidaDTO get(@PathVariable Integer id) {
		PostagemDAO dao = DAOFactory.getPostagemDAO();
		Postagem postagem = dao.buscaPorId(id);
		dao.close();
		
		DuvidaDTO duvidaDTO = new DuvidaDTO(postagem);
		return duvidaDTO;
	}
}
