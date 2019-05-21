package br.edu.ifrn.scatalapi.dao;

import br.edu.ifrn.scatalapi.model.Postagem;

public interface PostagemDAO extends AbstractDAO<Postagem>{

	public Postagem buscaPorId(Integer id);

}
