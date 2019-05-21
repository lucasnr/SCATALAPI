package br.edu.ifrn.scatalapi.dao;

import br.edu.ifrn.scatalapi.model.Curso;

public interface CursoDAO extends AbstractDAO<Curso> {

	public Curso buscaPorCodigoSUAP(String codigoSUAP);

	public Curso buscaPorNome(String nome);
}
