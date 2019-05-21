package br.edu.ifrn.scatalapi.dao.hibernate;

import br.edu.ifrn.scatalapi.dao.DAOFactory;
import br.edu.ifrn.scatalapi.dao.DisciplinaDAO;
import br.edu.ifrn.scatalapi.dao.TutoriaDAO;
import br.edu.ifrn.scatalapi.model.Tutoria;

public class TutoriaHibernateDAO extends AbstractHibernateDAO<Tutoria> implements TutoriaDAO {

	public TutoriaHibernateDAO() {
		super(Tutoria.class);
	}

	@Override
	public Tutoria buscaPorNomeDaDisciplina(String nomeDaDisciplina) {
		DisciplinaDAO disciplinaDAO = new DAOFactory().getDisciplinaDAO();
		Tutoria tutoria = disciplinaDAO.buscaPorNome(nomeDaDisciplina).getTutoria();
		disciplinaDAO.close();
		return tutoria;
	}

}
