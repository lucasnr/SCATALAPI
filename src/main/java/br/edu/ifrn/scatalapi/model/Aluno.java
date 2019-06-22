package br.edu.ifrn.scatalapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.edu.ifrn.scatalapi.util.CalendarUtil;
import br.edu.ifrn.suapi.ClienteSUAP;
import br.edu.ifrn.suapi.model.AlunoSUAP;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString(exclude= {"curso", "emails", "postagens", "clienteSUAP"})
@EqualsAndHashCode(exclude= {"curso", "emails", "postagens", "clienteSUAP"})

@Entity
@Table(name = "ALUNO")
public class Aluno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "NOME_USUAL")
	private String nomeUsual;
	
	@Column(name = "MATRICULA", unique = true, columnDefinition = "CHAR(14)")
	private String matricula;
	
	@Column(name = "URL_FOTO")
	private String urlFoto;
	
	@Column(name = "REGISTRO")
	private Date registro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURSO_ID")
	@Setter private Curso curso;

	@OneToMany(mappedBy = "aluno", fetch = FetchType.LAZY)
	private Set<Email> emails = new HashSet<>();

	@OneToMany(mappedBy = "criador", fetch = FetchType.LAZY)
	@OrderBy("registro desc")
	private List<Postagem> postagens = new ArrayList<>();

	@Transient
	private transient ClienteSUAP clienteSUAP;

	@PrePersist
	private void prePersist() {
		this.registro = CalendarUtil.now();
	}

	public Aluno(String nome, String matricula, String urlFoto, Curso curso) {
		super();
		this.nome = nome;
		this.matricula = matricula;
		this.urlFoto = urlFoto;
		this.curso = curso;
	}

	public Aluno(AlunoSUAP aluno) {
		this.nome = aluno.getVinculo().getNome();
		this.matricula = aluno.getMatricula();
		this.curso = new Curso(aluno.getCurso());
		this.urlFoto = aluno.getUrlFoto();
		this.nomeUsual = aluno.getNomeUsual();

		String endereco = aluno.getEmail();
		Email email = new Email(endereco, this);
		this.addEmail(email);

		this.clienteSUAP = aluno.getClienteSUAP();
	}

	public void addEmail(Email email) {
		email.setAluno(this);
		if (!this.emails.contains(email))
			this.emails.add(email);
	}

	public void addPostagem(Postagem postagem) {
		postagem.setCriador(this);
		if (!this.postagens.contains(postagem) && postagem.getId() == null)
			this.postagens.add(postagem);
	}

	public void setClienteSUAP(ClienteSUAP cliente) {
		if (this.clienteSUAP == null) {
			this.clienteSUAP = cliente;
		}
	}

	public Aluno(Integer id) {
		this.id = id;
	}

}
