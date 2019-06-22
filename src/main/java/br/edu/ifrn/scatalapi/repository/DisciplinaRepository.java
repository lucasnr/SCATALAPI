package br.edu.ifrn.scatalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.scatalapi.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer>{

	public Disciplina findByNome(String nome);
	
	public Disciplina findByNomeUsual(String nomeUsual);
}
