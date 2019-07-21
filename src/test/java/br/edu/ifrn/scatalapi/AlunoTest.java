package br.edu.ifrn.scatalapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.edu.ifrn.scatalapi.model.dto.AlunoResponseDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaRequestDTO;
import br.edu.ifrn.scatalapi.model.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.restclient.Pagina;
import br.edu.ifrn.scatalapi.restclient.Resposta;
import br.edu.ifrn.scatalapi.restclient.RestClient;

public class AlunoTest {

	private final RestClient client = new RestClient("http://localhost:8080/");
	
	@Test
	public void buscaAlunosEmPaginaDeTresEDescobreSeTemConteudo() throws JsonParseException, JsonMappingException, IOException {
		Pagina<AlunoResponseDTO> pagina = client.addQueryParam("size", 3)
				.doGet("aluno", new TypeReference<Pagina<AlunoResponseDTO>>(){}).getEntidade();
		
		assertEquals(3, pagina.getTamanho());
		assertTrue(pagina.temConteudo());
	}
	
	@Test
	public void buscaAlunoComIdIgualAUm() {
		AlunoResponseDTO alunoComIdIgualAUm = client.doGet("aluno/1", AlunoResponseDTO.class).getEntidade();
		boolean idIgualAUm = alunoComIdIgualAUm.getId() == 1;
		assertTrue(idIgualAUm);
	}
	
	@Test
	public void buscaAlunoComNomeLucasUsandoMatricula20161164010023() {
		AlunoResponseDTO aluno = client.doGet("aluno/matricula/20161164010023", AlunoResponseDTO.class).getEntidade();
		assertEquals("Lucas do Nascimento Ribeiro", aluno.getNome());
	}
	
	@Test
	public void buscaDuvidasDoAlunoComIdDois() throws JsonParseException, JsonMappingException, IOException {
		Resposta<Pagina<DuvidaResponseDTO>> resposta = 
				client.doGet("aluno/2/duvida", new TypeReference<Pagina<DuvidaResponseDTO>>() {});
		assertNotNull(resposta);
		
		boolean okOrNoContent = resposta.getStatus() == 200 || resposta.getStatus() == 204;
		assertTrue(okOrNoContent);
	}
	
	@Test
	public void criaDuvidaERespostaRetorna201EOHeaderLocationEDepoisDeleta() {
		DuvidaRequestDTO duvida = new DuvidaRequestDTO();
		duvida.setIdDaTutoria(1);
		duvida.setTitulo("Duvida do JUnit");
		duvida.setDescricao("Testando a criação de uma dúvida via JUnit");
		
		Resposta<DuvidaResponseDTO> resposta = client.doPost("aluno/1/duvida", duvida, DuvidaResponseDTO.class);
		assertEquals(201, resposta.getStatus());
		String location = (String) resposta.getHeaders().get("Location").get(0);
		assertNotNull(location);
		assertNotNull(resposta.getEntidade());
		
		int status = new RestClient(location).doDelete("").getStatus();
		assertEquals(204, status);
	}
	
}
