package br.edu.ifrn.scatalapi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude= {"curso", "tutoria"})
@EqualsAndHashCode(exclude= {"curso", "tutoria"})

@Entity
@Table(name = "DISCIPLINA")
public class Disciplina implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "DESCRICAO", columnDefinition = "TEXT")
	private String descricao;
	
	@Column(name = "NOME_USUAL")
	private String nomeUsual;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CURSO_ID")
	private Curso curso;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "disciplina")
	private Tutoria tutoria;

	public Disciplina(String nome, String descricao, Curso curso) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.curso = curso;
	}

}
