package br.edu.ifrn.scatalapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import br.edu.ifrn.scatalapi.util.CalendarUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@ToString(exclude= {"criador", "tutoria", "respostas", "postagemPai"})
@EqualsAndHashCode

@Entity
@Table(name = "POSTAGEM")
public class Postagem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "TITULO")
	@Setter private String titulo;
	
	@Column(name = "DESCRICAO", columnDefinition = "TEXT")
	@Setter private String descricao;
	
	@Column(name = "REGISTRO")
	private Date registro;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ALUNO_ID")
	private Aluno criador;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TUTORIA_ID")
	@Setter private Tutoria tutoria;

	@OneToMany(mappedBy = "postagemPai", fetch = FetchType.LAZY)
	private List<Postagem> respostas = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "POSTAGEM_PAI")
	@Setter private Postagem postagemPai;

	@PrePersist
	public void prePersist() {
		this.registro = CalendarUtil.now();
	}

	public Postagem(String titulo, String descricao) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
	}

	public void addResposta(Postagem resposta) {
		if (resposta.getPostagemPai() == null && this.respostas != null && ! this.respostas.contains(resposta)) {
			resposta.setPostagemPai(this);
			this.respostas.add(resposta);			
		}
	}

	public void setCriador(Aluno criador) {
		if (this.criador == null) {			
			this.criador = criador;
		}
	}
	
}