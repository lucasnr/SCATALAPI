package br.edu.ifrn.scatalapi.dao;

import br.edu.ifrn.scatalapi.model.Tutoria;

public interface TutoriaDAO extends AbstractDAO<Tutoria> {

	public Tutoria buscaPorNomeDaDisciplina(String disciplinaNome);
	
	public Tutoria buscaPorNomeUsualDaDisciplina(String nomeUsualDaDisciplina);
}
