package br.edu.ifrn.scatalapi.model.dto;

import br.edu.ifrn.scatalapi.model.Aluno;
import lombok.Data;

@Data
public class AlunoDTO {

	private String nome;
	private String matricula;
	private String nomeUsual;
	private String urlFoto;

	public AlunoDTO(Aluno aluno) {
		this.nome = aluno.getNome();
		this.nomeUsual = aluno.getNomeUsual();
		this.matricula = aluno.getMatricula();
		this.urlFoto = aluno.getUrlFoto();
	}

}
