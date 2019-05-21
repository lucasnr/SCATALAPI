package br.edu.ifrn.scatalapi.dao.hibernate;

import br.edu.ifrn.scatalapi.dao.TutoriaDAO;
import br.edu.ifrn.scatalapi.model.Tutoria;

public class TutoriaHibernateDAO extends AbstractHibernateDAO<Tutoria> implements TutoriaDAO {

	public TutoriaHibernateDAO() {
		super(Tutoria.class);
	}

}
