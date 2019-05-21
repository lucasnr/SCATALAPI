package br.edu.ifrn.scatalapi.dao.hibernate;

import br.edu.ifrn.scatalapi.dao.CursoDAO;
import br.edu.ifrn.scatalapi.model.Curso;

public class CursoHibernateDAO extends AbstractHibernateDAO<Curso> implements CursoDAO {

	public CursoHibernateDAO() {
		super(Curso.class);
	}

	@Override
	public final Curso buscaPorCodigoSUAP(String codigoSUAP) {
		return super.buscaPorCampoUnico("codigoSUAP", codigoSUAP);
	}

	@Override
	public Curso buscaPorNome(String nome) {
		return super.buscaPorCampoUnico("nome", nome);
	}
}
