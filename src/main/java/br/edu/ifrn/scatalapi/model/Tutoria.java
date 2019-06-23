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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(exclude= {"tutores", "disciplina", "postagens"})
@EqualsAndHashCode(exclude= {"tutores", "disciplina", "postagens"})

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
	@Setter private List<Aluno> tutores;

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

}
