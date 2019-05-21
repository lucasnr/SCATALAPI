package br.edu.ifrn.scatalapi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import br.edu.ifrn.scatalapi.util.CalendarUtil;

@Entity
@Table(name = "EMAIL")
public class Email implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "ENDERECO")
	private String endereco;
	@Column(name = "REGISTRO")
	private Date registro;
	
	@ManyToOne
	@JoinColumn(name = "ALUNO_ID")
	private Aluno aluno;

	@PrePersist
	private void prePersist() {
		this.registro = CalendarUtil.now();
	}

	public Email(String endereco, Aluno aluno) {
		this.endereco = endereco;
		this.aluno = aluno;
	}

	public Email(String endereco) {
		this.endereco = endereco;
	}

	public Email() {
	}

	public Integer getId() {
		return id;
	}

	public String getEndereco() {
		return endereco;
	}

	public Date getRegistro() {
		return registro;
	}

	public Aluno getAluno() {
		return aluno;
	}
	
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((registro == null) ? 0 : registro.hashCode());
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
		Email other = (Email) obj;
		if (other.endereco.equals(this.endereco) || other.id.equals(this.id))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "Email [id=" + id + ", endereco=" + endereco + ", registro=" + registro + "]";
	}

}
