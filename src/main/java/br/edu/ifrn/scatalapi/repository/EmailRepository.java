package br.edu.ifrn.scatalapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifrn.scatalapi.model.Email;

public interface EmailRepository extends JpaRepository<Email, Integer>{

}
