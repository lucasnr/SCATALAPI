package br.edu.ifrn.scatalapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntidadeService<T> {
	
	public Optional<T> findById(Integer id);

	public Page<T> findAll(Pageable pageable);
}
