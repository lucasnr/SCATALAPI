package br.edu.ifrn.scatalapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import br.edu.ifrn.scatalapi.model.dto.MatriculaDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaDetalhadaResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.TutoriaTutoresUpdateDTO;
import br.edu.ifrn.scatalapi.restclient.Resposta;
import br.edu.ifrn.scatalapi.restclient.RestClient;

public class TutoriaTest {

	private RestClient client = new RestClient("http://localhost:8080/");
	
	@Test
	public void atualizaTutores() {
		TutoriaTutoresUpdateDTO novosTutores = new TutoriaTutoresUpdateDTO();
		novosTutores.setTutores(Arrays.asList(new MatriculaDTO("20161164010026")));
		Resposta<TutoriaDetalhadaResponseDTO> resposta = 
				client.doPut("tutoria/1/tutor", novosTutores, TutoriaDetalhadaResponseDTO.class);
		
		assertEquals(200, resposta.getStatus());
		assertNotNull(resposta.getEntidade());
	}
}
