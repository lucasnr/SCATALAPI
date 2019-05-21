package br.edu.ifrn.scatalapi.model.dto;

public class DuvidaDTO {
	private String titulo;
	private String descricao;
	private Integer idDoAluno;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getIdDoAluno() {
		return idDoAluno;
	}

	public void setIdDoAluno(Integer alunoId) {
		this.idDoAluno = alunoId;
	}

}
