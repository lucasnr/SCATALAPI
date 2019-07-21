package br.edu.ifrn.scatalapi.restclient;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Pagina<T> {
	
	@JsonProperty("number")
	private int numero;

	@JsonProperty("size")
	private int tamanho;

	@JsonProperty("totalPages")
	private int totalDePaginas;

	@JsonProperty("numberOfElements")
	private int numeroDeElementos;

	@JsonProperty("totalElements")
	private long totalDeElementos;

	@JsonProperty("previousPage")
	private boolean paginaAnterior;

	@JsonProperty("firstPage")
	private boolean primeiraPagina;

	@JsonProperty("nextPage")
	private boolean proximaPagina;

	@JsonProperty("lastPage")
	private boolean ultimaPagina;

	@JsonProperty("content")
	private List<T> conteudo;

	public boolean temConteudo() {
		return ! this.conteudo.isEmpty();
	}

}