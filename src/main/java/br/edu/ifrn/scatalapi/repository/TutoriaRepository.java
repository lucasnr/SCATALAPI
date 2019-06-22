package br.edu.ifrn.scatalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.scatalapi.model.Tutoria;

public interface TutoriaRepository extends JpaRepository<Tutoria, Integer>{

	public Tutoria findByDisciplinaNome(String nomeDaDisciplina);
	
	public Tutoria findByDisciplinaNomeUsual(String nomeUsualDaDisciplina);
}
