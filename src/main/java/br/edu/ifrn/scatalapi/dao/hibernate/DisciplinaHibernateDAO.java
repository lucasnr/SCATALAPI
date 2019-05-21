package br.edu.ifrn.scatalapi.dao.hibernate;

import br.edu.ifrn.scatalapi.dao.DisciplinaDAO;
import br.edu.ifrn.scatalapi.model.Disciplina;

public class DisciplinaHibernateDAO extends AbstractHibernateDAO<Disciplina> implements DisciplinaDAO{

	public DisciplinaHibernateDAO() {
		super(Disciplina.class);
	}

	@Override
	public Disciplina buscaPorNome(String nome) {
		return super.buscaPorCampoUnico("nome", nome);
	}

	@Override
	public Disciplina buscaPorId(Integer id) {
		return super.buscarPorId(id);
	}

}
