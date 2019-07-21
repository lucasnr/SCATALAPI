package br.edu.ifrn.scatalapi.restclient;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = { "entidade" })
@EqualsAndHashCode(exclude = { "entidade" })
public class Resposta<T> {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private T entidade;
	
	private int status;
	
	private MultivaluedMap<String, Object> headers;
	
	private Set<String> allowedMethods;
	
	private LocalDate date;
	
	private int length;
	
	private URI location;

	public Resposta(Response response, Class<T> type) {
		try {			
			this.entidade = response.readEntity(type);
		} catch (Exception e) {
			this.entidade = null;
		}
		init(response);
	}

	public Resposta(Response response, TypeReference<T> type) {
		try {			
			this.entidade = OBJECT_MAPPER.readValue(response.readEntity(String.class), type);
		} catch (Exception e) {
			this.entidade = null;
		}
		init(response);
	}

	private void init(Response response) {
		this.status = response.getStatus();
		this.headers = response.getHeaders();
		this.allowedMethods = response.getAllowedMethods();
		this.date = response.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.length = response.getLength();
		this.location = response.getLocation();
	}
}
