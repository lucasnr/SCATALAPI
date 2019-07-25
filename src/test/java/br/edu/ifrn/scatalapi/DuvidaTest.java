package br.edu.ifrn.scatalapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

import br.edu.ifrn.scatalapi.dto.DuvidaResponseDTO;
import br.edu.ifrn.scatalapi.dto.DuvidaUpdateDTO;
import br.edu.ifrn.scatalapi.dto.RespostaRequestDTO;
import br.edu.ifrn.scatalapi.dto.RespostaResponseDTO;
import br.edu.ifrn.scatalapi.restclient.Pagina;
import br.edu.ifrn.scatalapi.restclient.Resposta;
import br.edu.ifrn.scatalapi.restclient.RestClient;

public class DuvidaTest {

	private final RestClient client = RestClientBuilder.newRestClient();
	
	@Test
	public void atualizaDuvidaERespostaRetorna200() {
		DuvidaUpdateDTO duvida = new DuvidaUpdateDTO();
		duvida.setTitulo("Duvida do JUnit editada");
		duvida.setDescricao("Testando a edi��o de uma d�vida vi JUnit");
		
		Resposta<DuvidaResponseDTO> resposta = client.doPatch("duvida/13", duvida, DuvidaResponseDTO.class);
		DuvidaResponseDTO duvidaResposta = resposta.getEntidade();
		
		assertEquals(200, resposta.getStatus());
		assertNotNull(duvidaResposta);
		assertEquals(duvida.getTitulo(), duvidaResposta.getTitulo());
	}
	
	@Test
	public void buscaDuvidaComIdTreze() {
		Resposta<DuvidaResponseDTO> resposta = client.doGet("duvida/13", DuvidaResponseDTO.class);
		assertEquals(200, resposta.getStatus());
		
		DuvidaResponseDTO duvida = resposta.getEntidade();
		assertNotNull(duvida);
	}
	
	@Test
	public void buscaDuvidaQueNaoExiste() {
		Resposta<DuvidaResponseDTO> resposta = client.doGet("duvida/9999", DuvidaResponseDTO.class);
		assertEquals(404, resposta.getStatus());
		assertNull(resposta.getEntidade());
	}
	
	@Test
	public void buscaUmaDuvidaUsandoAlgumaQuery() {
		Resposta<Pagina<DuvidaResponseDTO>> resposta = 
				client.doGet("duvida/search/energia", new TypeReference<Pagina<DuvidaResponseDTO>>() {});
		assertTrue(resposta.getStatus() == 200 || resposta.getStatus() == 204);
	}
	
	@Test
	public void buscaRespostasDaDuvidaComIdTreze() {
		Resposta<Pagina<RespostaResponseDTO>> resposta = 
				client.doGet("duvida/13/resposta", new TypeReference<Pagina<RespostaResponseDTO>>() {});
		assertTrue(resposta.getStatus() == 200 || resposta.getStatus() == 204);
	}
	
	@Test
	public void criaRespostaParaDuvidaComIdTrezeEDeletaDepois() {
		RespostaRequestDTO respostaDTO = new RespostaRequestDTO();
		respostaDTO.setDescricao("Respondendo a d�vida via JUnit");
		respostaDTO.setIdDoAluno(1);
		
		Resposta<RespostaResponseDTO> resposta = client.doPost("duvida/13/resposta", respostaDTO, RespostaResponseDTO.class);
		assertEquals(201, resposta.getStatus());
		assertNotNull(resposta.getEntidade());
		String location = (String) resposta.getHeaders().get("Location").get(0);
		assertNotNull(location);
		
		int status = new RestClient(location).addAuthorizationHeader(this.client.getAuth()).doDelete("").getStatus();
		assertEquals(204, status);
	}
}
