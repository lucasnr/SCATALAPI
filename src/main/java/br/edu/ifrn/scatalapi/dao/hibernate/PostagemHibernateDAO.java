package br.edu.ifrn.scatalapi.dao.hibernate;

import br.edu.ifrn.scatalapi.dao.PostagemDAO;
import br.edu.ifrn.scatalapi.model.Postagem;

public class PostagemHibernateDAO extends AbstractHibernateDAO<Postagem> implements PostagemDAO{

	public PostagemHibernateDAO() {
		super(Postagem.class);
	}

	@Override
	public Postagem buscaPorId(Integer id) {
		return super.buscarPorId(id);
	}
}
