package br.edu.ifrn.scatalapi.model.dto;

public class Credenciais {

	private String matricula;
	private String senha;

	public Credenciais(String matricula, String senha) {
		super();
		this.matricula = matricula;
		this.senha = senha;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getSenha() {
		return senha;
	}

}
