package br.edu.ifrn.scatalapi.dao;

import br.edu.ifrn.scatalapi.model.Disciplina;

public interface DisciplinaDAO extends AbstractDAO<Disciplina>{

	public Disciplina buscaPorNome(String nome);
	
	public Disciplina buscaPorNomeUsual(String nomeUsual);

	public Disciplina buscaPorId(Integer disciplinaId);
}
