package br.edu.ifrn.scatalapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.edu.ifrn.suapi.model.CursoSUAP;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor

@Entity
@Table(name = "CURSO")
public class Curso implements Serializable {

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
	
	@Column(name = "CODIGO_SUAP")
	private String codigoSUAP;

	@OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
	private List<Disciplina> disciplinas = new ArrayList<>();

	public Curso(String nome, String codigoSUAP) {
		super();
		this.nome = nome;
		this.codigoSUAP = codigoSUAP;
	}
	
	public Curso(CursoSUAP cursoSUAP) {
		this.nome = cursoSUAP.getDescricao();
		this.codigoSUAP = cursoSUAP.getCodigo();
	}

}
