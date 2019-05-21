package br.edu.ifrn.scatalapi.dao;

import br.edu.ifrn.scatalapi.model.Aluno;

/**
 * Abstração que define os comportamentos de um DAO da entidade ALUNO
 * 
 * @author Lucas do Nascimento Ribeiro
 * @since 1.0
 * @version 1.0
 */
public interface AlunoDAO extends AbstractDAO<Aluno> {

	/**
	 * Retorna um aluno especificado a partir de sua matrícula, ou nulo caso não
	 * exista
	 * 
	 * @param matricula A matricula do aluno a ser buscado
	 * @return Aluno encontrado com a matrícula especificada
	 */
	public Aluno buscaPorMatricula(String matricula);

	/**
	 * Busca registro de um aluno a partir do ID do mesmo
	 */
	public Aluno buscaPorId(Integer id);
}
