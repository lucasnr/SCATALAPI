package br.edu.ifrn.scatalapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.scatalapi.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Integer>{

	public Optional<Aluno> findByMatricula(String matricula);
}
