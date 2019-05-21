package br.edu.ifrn.scatalapi.dao;

import br.edu.ifrn.scatalapi.dao.hibernate.AlunoHibernateDAO;
import br.edu.ifrn.scatalapi.dao.hibernate.CursoHibernateDAO;
import br.edu.ifrn.scatalapi.dao.hibernate.DisciplinaHibernateDAO;
import br.edu.ifrn.scatalapi.dao.hibernate.PostagemHibernateDAO;
import br.edu.ifrn.scatalapi.dao.hibernate.TutoriaHibernateDAO;

public class DAOFactory {

	public AlunoDAO getAlunoDAO() {
		AlunoDAO dao = new AlunoHibernateDAO();
		return dao;
	}
	
	public CursoDAO getCursoDAO() {
		CursoDAO dao = new CursoHibernateDAO();
		return dao;
	}
	
	public DisciplinaDAO getDisciplinaDAO() {
		DisciplinaDAO dao = new DisciplinaHibernateDAO();
		return dao;
	}
	
	public PostagemDAO getPostagemDAO() {
		PostagemDAO dao = new PostagemHibernateDAO();
		return dao;
	}
	
	public TutoriaDAO getTutoriaDAO() {
		TutoriaDAO dao = new TutoriaHibernateDAO();
		return dao;
	}
}
