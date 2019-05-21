package br.edu.ifrn.scatalapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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

	public Curso() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCodigoSUAP() {
		return codigoSUAP;
	}

	public List<Disciplina> getDisciplinas() {
		return Collections.unmodifiableList(disciplinas);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigoSUAP == null) ? 0 : codigoSUAP.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		if (codigoSUAP == null) {
			if (other.codigoSUAP != null)
				return false;
		} else if (!codigoSUAP.equals(other.codigoSUAP))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Curso [id=" + id + ", nome=" + nome + ", codigoSUAP=" + codigoSUAP + "]";
	}

}
