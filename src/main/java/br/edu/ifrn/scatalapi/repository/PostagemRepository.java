package br.edu.ifrn.scatalapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifrn.scatalapi.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Integer>{

	@Query("SELECT p FROM Postagem p WHERE postagemPai IS NULL AND p.tutoria.disciplina.nomeUsual = :nomeUsual")
	public Page<Postagem> findDuvidasByDisciplina(Pageable paginacao, @Param("nomeUsual") String disciplinaUsual);

}
