package br.edu.ifrn.scatalapi.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import br.edu.ifrn.scatalapi.dao.AbstractDAO;

public class AbstractHibernateDAO<T> implements AbstractDAO<T>{
	protected Session session;
	protected final Class<T> clazz;

	public final void init() {
		this.session = SessionFactorySingleton.getInstance().openSession();
	}

	public AbstractHibernateDAO(Class<T> clazz) {
		this.clazz = clazz;
		init();
	}

	@Override
	public boolean salvar(T objeto) {
		Runnable acao = () -> this.session.save(objeto);
		return executa(objeto, acao);
	}

	@Override
	public boolean atualizar(T objeto) {
		Runnable acao = () -> this.session.update(objeto);
		return executa(objeto, acao);
	}

	@Override
	public final boolean remover(T objeto) {
		Runnable acao = () -> this.session.delete(objeto);
		return executa(objeto, acao);
	}

	protected final boolean executa(T objeto, Runnable acao) {
		boolean retorno = false;
		session.getTransaction().begin();
		try {
			acao.run();
			session.getTransaction().commit();
			retorno = true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		}
		return retorno;
	}

	@Override
	public final List<T> listar() {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);

		criteria.select(root);
//		criteria.where(builder.like(root.get("nome"), "Lucas%"));

		Query<T> query2 = session.createQuery(criteria);
		List<T> lista = query2.list();
		return lista;
	}
	
	protected final T buscaPorCampoUnico(String campo, String valor) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);

		Root<T> root = criteria.from(clazz);
		Path<T> path = root.get(campo);
		Predicate equal = builder.equal(path, valor);
		criteria.where(equal);

		Query<T> query = session.createQuery(criteria);
		T resultado = query.uniqueResult();
		return resultado;
	}
	
	protected final T buscarPorId(Integer id) {
		return session.find(clazz, id);
	}

	public final boolean close() {
		try {
			this.session.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}