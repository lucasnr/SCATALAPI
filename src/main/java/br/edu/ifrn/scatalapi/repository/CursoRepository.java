package br.edu.ifrn.scatalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.scatalapi.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer>{

	public Curso findByCodigoSUAP(String codigoSUAP);

	public Curso findByNome(String nome);
}
