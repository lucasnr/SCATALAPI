package br.edu.ifrn.scatalapi.restclient;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = {"client"})
@EqualsAndHashCode
public class RestClient {
	private static final String MEDIA_TYPE = MediaType.APPLICATION_JSON;

	@Getter
	private final String TARGET;

	@Getter
	private final Client client = ClientBuilder.newClient();
	
	private final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

	@Getter
	private final Map<String, Object> params = new HashMap<>();

	public RestClient(String target) {
		this.TARGET = target;
		this.addHeader("Content-Type", MEDIA_TYPE);
		this.addHeader("Accept", MEDIA_TYPE);
	}	
	
	public Response doGet(String path) {
		return request(path).get();
	}

	public <T> Resposta<T> doGet(String path, Class<T> responseType) {
		return new Resposta<T>(doGet(path), responseType);
	}
	
	public <T> Resposta<T> doGet(String path, TypeReference<T> typeReference) {
		return new Resposta<T>(doGet(path), typeReference);
	}
	
	public <E> Response doPost(String path, E entity) {
		return request(path).post(createEntity(entity));
	}

	public <T, E> Resposta<T> doPost(String path, E entity, Class<T> responseType) {
		return new Resposta<T>(doPost(path, entity), responseType);
	}
	
	public <T, E> Resposta<T> doPost(String path, E entity, TypeReference<T> typeReference) {
		return new Resposta<T>(doPost(path, entity), typeReference);
	}
	
	public <E> Response doPut(String path, E entity) {
		return request(path).put(createEntity(entity));
	}
	
	public <T, E> Resposta<T> doPut(String path, E entity, Class<T> responseType) {
		return new Resposta<T>(doPut(path, entity), responseType);
	}
	
	public <T, E> Resposta<T> doPut(String path, E entity, TypeReference<T> typeReference) {
		return new Resposta<T>(doPut(path, entity), typeReference);
	}
	
	public <E> Response doPatch(String path, E entity) {
		return request(path).method("PATCH", createEntity(entity));
	}
	
	public <T, E> Resposta<T> doPatch(String path, E entity, Class<T> responseType) {
		return new Resposta<T>(doPatch(path, entity), responseType);
	}
	
	public <T, E> Resposta<T> doPatch(String path, E entity, TypeReference<T> typeReference) {
		return new Resposta<T>(doPatch(path, entity), typeReference);
	}
	
	public Response doDelete(String path) {
		return request(path).delete();
	}
	
	private Builder request(String path) {
		WebTarget target = client.target(TARGET).path(path);
		target.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);

		for (Entry<String, Object> entry : params.entrySet())
			target = target.queryParam(entry.getKey(), entry.getValue());
		params.clear();
		
		return target.request().headers(headers);
	}
	
	private <E> Entity<E> createEntity(E entity) {
		return Entity.entity(entity, MEDIA_TYPE);
	}

	public void addHeader(String name, String value) {
		this.headers.putSingle(name, value);
	}
	
	public RestClient addQueryParam(String name, Object value){
		this.params.put(name, value);
		return this;
	}
}
