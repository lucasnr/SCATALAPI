package br.edu.ifrn.scatalapi.dao.hibernate;

import org.hibernate.query.Query;

import br.edu.ifrn.scatalapi.dao.TutoriaDAO;
import br.edu.ifrn.scatalapi.model.Tutoria;

public class TutoriaHibernateDAO extends AbstractHibernateDAO<Tutoria> implements TutoriaDAO {

	public TutoriaHibernateDAO() {
		super(Tutoria.class);
	}

	@Override
	public Tutoria buscaPorNomeDaDisciplina(String nomeDaDisciplina) {
		Tutoria tutoria = null;
		
//		DisciplinaDAO disciplinaDAO = DAOFactory.getDisciplinaDAO();
//		Disciplina disciplina = disciplinaDAO.buscaPorNome(nomeDaDisciplina);
//		if (disciplina == null) {
//			return null;
//		}
//		
//		tutoria = disciplina.getTutoria();
//		disciplinaDAO.close();
		
		Query<Tutoria> query = super.session.createQuery("SELECT t FROM Tutoria t WHERE t.disciplina.nome = :nomeDaDisciplina", clazz);
		query.setParameter("nomeDaDisciplina", nomeDaDisciplina);
		tutoria = query.uniqueResult();
		
		return tutoria;
	}

	@Override
	public Tutoria buscaPorNomeUsualDaDisciplina(String nomeUsualDaDisciplina) {
		Query<Tutoria> query = super.session.createQuery("SELECT t FROM Tutoria t WHERE t.disciplina.nomeUsual = :nomeUsualDaDisciplina", clazz);
		query.setParameter("nomeUsualDaDisciplina", nomeUsualDaDisciplina);
		Tutoria tutoria = query.uniqueResult();
		return tutoria;
	}

}
