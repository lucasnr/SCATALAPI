package br.edu.ifrn.scatalapi.dao;

import java.util.List;

/**
 * Abstra��o geral de um DAO, toda classe DAO precisa implementar esta interface
 * 
 * @author Lucas do Nascimento Ribeiro
 * @since 1.0
 * @version 1.0
 * */
public interface AbstractDAO<T> {
	
	/**
	 * Salva uma entidade no banco de dados, fazendo uma inser��o
	 * 
	 * @param objeto Objeto a ser inserido como registro no banco de dados
	 * @return Valor booleano indicando se a inser��o teve sucesso
	 * */
	public boolean salvar(T objeto);

	/**
	 * Atualiza uma entidade no banco de dados
	 * 
	 * @param objeto Objeto a ter o registro atualizado no banco de dados
	 * @return Valor booleano indicando se a atualiza��o teve sucesso
	 * */
	public boolean atualizar(T objeto);
	

	/**
	 * Deleta uma entidade do banco de dados, fazendo uma remo��o
	 * 
	 * @param objeto Objeto a ter seu registro deletado do banco de dados
	 * @return Valor booleano indicando se a remo��o teve sucesso
	 * */
	public boolean remover(T objeto);

	/**
	 * Seleciona todos os registros da entidade no banco de dados
	 * 
	 * @return Lista da entidade com todos os registros do banco de dados
	 * */
	public List<T> listar();

	/**
	 * Fecha a conex�o atual
	 * 
	 * @return Valor booleano indicando se a conex�o foi fechada
	 * */
	public boolean close();
}
