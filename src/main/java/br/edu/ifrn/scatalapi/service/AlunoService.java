package br.edu.ifrn.scatalapi.service;

import java.util.Optional;

import br.edu.ifrn.scatalapi.model.Aluno;

public interface AlunoService extends EntidadeService<Aluno> {

	public Optional<Aluno> findByMatricula(String matricula);
}
