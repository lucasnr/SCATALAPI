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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode

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
	@Setter private Aluno aluno;

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

}
