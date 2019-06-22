package br.edu.ifrn.scatalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.scatalapi.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Integer>{

	public Aluno findByMatricula(String matricula);
}
