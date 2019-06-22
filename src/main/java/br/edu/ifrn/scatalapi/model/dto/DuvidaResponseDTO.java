package br.edu.ifrn.scatalapi.model.dto;

import java.util.Date;

import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Postagem;
import lombok.Data;

@Data
public class DuvidaResponseDTO {

	private Integer id;
	
	private String titulo;
	
	private String descricao;
	
	private String aluno;
	
	private String matricula;
	
	private Date registro;
	
	public DuvidaResponseDTO(Postagem postagem) {
		this.id = postagem.getId();
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.aluno = postagem.getCriador().getNome();
		this.matricula = postagem.getCriador().getMatricula();
		this.registro = postagem.getRegistro();
	}

	public DuvidaResponseDTO(Postagem postagem, Aluno aluno) {
		this.id = postagem.getId();
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.aluno = aluno.getNome();
		this.matricula = aluno.getMatricula();
		this.registro = postagem.getRegistro();
	}
}
