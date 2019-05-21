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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "TUTORIA")
public class Tutoria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "TUTOR", joinColumns = {
			@JoinColumn(name = "TUTORIA_ID") }, inverseJoinColumns = @JoinColumn(name = "ALUNO_ID"))
	private List<Aluno> tutores;

	@OneToOne
	@JoinColumn(name = "DISCIPLINA_ID")
	private Disciplina disciplina;

	@OneToMany(mappedBy = "tutoria", fetch = FetchType.LAZY)
	@OrderBy("registro desc")
	private List<Postagem> postagens;

	public Tutoria() {
		super();
		this.tutores = new ArrayList<>();
		this.postagens = new ArrayList<>();
	}

	public void addPostagem(Postagem postagem) {
		postagem.setTutoria(this);
		if (!this.postagens.contains(postagem) && postagem.getId() == null)
			this.postagens.add(postagem);
	}
	
	public void addTutor(Aluno aluno) {
		if (aluno != null && aluno.getId() != null && ! this.tutores.contains(aluno)) {			
			this.tutores.add(aluno);
		}
	}
	
	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public List<Postagem> getPostagens() {
		return postagens;
	}

	public Integer getId() {
		return id;
	}

	public List<Aluno> getTutores() {
		return tutores;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tutores == null) ? 0 : tutores.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((disciplina == null) ? 0 : disciplina.hashCode());
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
		Tutoria other = (Tutoria) obj;
		if (tutores == null) {
			if (other.tutores != null)
				return false;
		} else if (!tutores.equals(other.tutores))
			return false;
		if (disciplina == null) {
			if (other.disciplina != null)
				return false;
		} else if (!disciplina.equals(other.disciplina))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tutoria [id=" + id + ", disciplina=" + disciplina + "]";
	}

}
