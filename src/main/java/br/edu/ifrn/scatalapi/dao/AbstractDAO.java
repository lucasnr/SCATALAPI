package br.edu.ifrn.scatalapi.dao;

import java.util.List;

/**
 * Abstração geral de um DAO, toda classe DAO precisa implementar esta interface
 * 
 * @author Lucas do Nascimento Ribeiro
 * @since 1.0
 * @version 1.0
 * */
public interface AbstractDAO<T> {
	
	/**
	 * Salva uma entidade no banco de dados, fazendo uma inserção
	 * 
	 * @param objeto Objeto a ser inserido como registro no banco de dados
	 * @return Valor booleano indicando se a inserção teve sucesso
	 * */
	public boolean salvar(T objeto);

	/**
	 * Atualiza uma entidade no banco de dados
	 * 
	 * @param objeto Objeto a ter o registro atualizado no banco de dados
	 * @return Valor booleano indicando se a atualização teve sucesso
	 * */
	public boolean atualizar(T objeto);
	

	/**
	 * Deleta uma entidade do banco de dados, fazendo uma remoção
	 * 
	 * @param objeto Objeto a ter seu registro deletado do banco de dados
	 * @return Valor booleano indicando se a remoção teve sucesso
	 * */
	public boolean remover(T objeto);

	/**
	 * Seleciona todos os registros da entidade no banco de dados
	 * 
	 * @return Lista da entidade com todos os registros do banco de dados
	 * */
	public List<T> listar();

	/**
	 * Fecha a conexão atual
	 * 
	 * @return Valor booleano indicando se a conexão foi fechada
	 * */
	public boolean close();
}
