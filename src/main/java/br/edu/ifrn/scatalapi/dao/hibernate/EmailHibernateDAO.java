package br.edu.ifrn.scatalapi.dao.hibernate;

import br.edu.ifrn.scatalapi.model.Email;

public class EmailHibernateDAO extends AbstractHibernateDAO<Email> {

	public EmailHibernateDAO() {
		super(Email.class);
	}
}
