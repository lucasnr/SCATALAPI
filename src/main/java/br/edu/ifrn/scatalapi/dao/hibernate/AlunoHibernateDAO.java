package br.edu.ifrn.scatalapi.dao.hibernate;

import br.edu.ifrn.scatalapi.dao.AlunoDAO;
import br.edu.ifrn.scatalapi.dao.CursoDAO;
import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.model.Curso;
import br.edu.ifrn.scatalapi.model.Email;
import br.edu.ifrn.scatalapi.model.Postagem;

public class AlunoHibernateDAO extends AbstractHibernateDAO<Aluno> implements AlunoDAO{

	public AlunoHibernateDAO() {
		super(Aluno.class);
	}

	@Override
	public boolean salvar(Aluno objeto) {
		if (objeto.getEmails().isEmpty())
			return false;
			
		if(objeto.getCurso().getId() == null) {
			CursoDAO dao = new CursoHibernateDAO();
			Curso curso = dao.buscaPorCodigoSUAP(objeto.getCurso().getCodigoSUAP());
			objeto.setCurso(curso);
			if(curso == null)
				return false;
		}
		Runnable acao = () -> {
			session.save(objeto);
			
			for (Email email : objeto.getEmails())
				session.save(email);
		};
		return executa(objeto, acao);
	}
	
	@Override
	public boolean atualizar(Aluno objeto) {
		Runnable acao = () -> {
			for(Email email : objeto.getEmails()) {
				if(email.getId() == null)
					session.save(email);
			}
			for(Postagem enquete : objeto.getPostagens()) {
				if(enquete.getId() == null)
					session.save(enquete);
			}
		};
		return executa(objeto, acao);
	}

	@Override
	public Aluno buscaPorMatricula(String matricula) {
		return super.buscaPorCampoUnico("matricula", matricula);
		
//		TypedQuery<Aluno> query = session.createQuery("SELECT a FROM Aluno a WHERE a.matricula = :m", Aluno.class);
//		query.setParameter("m", matricula);
//		Aluno result = query.getSingleResult();
//		return result;
	}

	@Override
	public Aluno buscaPorId(Integer id) {
		return super.buscarPorId(id);
	}
}
