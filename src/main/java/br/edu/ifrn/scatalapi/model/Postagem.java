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
	private String titulo;
	@Column(name = "DESCRICAO", columnDefinition = "TEXT")
	private String descricao;
	@Column(name = "REGISTRO")
	private Date registro;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ALUNO_ID")
	private Aluno criador;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TUTORIA_ID")
	private Tutoria tutoria;

	@OneToMany(mappedBy = "postagemPai", fetch = FetchType.LAZY)
	private List<Postagem> respostas = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "POSTAGEM_PAI")
	private Postagem postagemPai;

	@PrePersist
	public void prePersist() {
		this.registro = CalendarUtil.now();
	}

	public Postagem(String titulo, String descricao) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
	}

	public Postagem() {
		super();
	}

	public void addResposta(Postagem resposta) {
		if (resposta.getPostagemPai() == null && this.respostas != null && ! this.respostas.contains(resposta)) {
			resposta.setPostagemPai(this);
			this.respostas.add(resposta);			
		}
	}

	public List<Postagem> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Postagem> respostas) {
		this.respostas = respostas;
	}

	public Integer getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Date getRegistro() {
		return registro;
	}

	public Aluno getCriador() {
		return criador;
	}
	
	public void setCriador(Aluno criador) {
		if (this.criador == null) {			
			this.criador = criador;
		}
	}

	public Tutoria getTutoria() {
		return tutoria;
	}
	
	public void setTutoria(Tutoria tutoria) {
		this.tutoria = tutoria;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Postagem getPostagemPai() {
		return postagemPai;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setPostagemPai(Postagem postagemPai) {
		this.postagemPai = postagemPai;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tutoria == null) ? 0 : tutoria.hashCode());
		result = prime * result + ((criador == null) ? 0 : criador.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((registro == null) ? 0 : registro.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		Postagem other = (Postagem) obj;
		if (tutoria == null) {
			if (other.tutoria != null)
				return false;
		} else if (!tutoria.equals(other.tutoria))
			return false;
		if (criador == null) {
			if (other.criador != null)
				return false;
		} else if (!criador.equals(other.criador))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (registro == null) {
			if (other.registro != null)
				return false;
		} else if (!registro.equals(other.registro))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Postagem [id=" + id + ", titulo=" + titulo + ", descricao=" + descricao + ", registro=" + registro + "]";
	}

}