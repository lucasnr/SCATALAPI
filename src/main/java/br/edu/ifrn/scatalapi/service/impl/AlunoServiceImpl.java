package br.edu.ifrn.scatalapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifrn.scatalapi.model.Aluno;
import br.edu.ifrn.scatalapi.repository.AlunoRepository;
import br.edu.ifrn.scatalapi.service.AlunoService;

@Service
public class AlunoServiceImpl implements AlunoService {

	@Autowired
	private AlunoRepository repository;
	
	@Override
	public Optional<Aluno> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public Page<Aluno> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Optional<Aluno> findByMatricula(String matricula) {
		return repository.findByMatricula(matricula);
	}

}
