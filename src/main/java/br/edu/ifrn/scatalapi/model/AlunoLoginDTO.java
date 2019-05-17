package br.edu.ifrn.scatalapi.model;

public class AlunoLoginDTO {

	private String matricula;
	private String senha;

	public AlunoLoginDTO(String matricula, String senha) {
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
