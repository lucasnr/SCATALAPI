package br.edu.ifrn.scatalapi.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactorySingleton {
	private static SessionFactory sessionFactory;

	public static SessionFactory getInstance() {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		return sessionFactory;
	}

}
