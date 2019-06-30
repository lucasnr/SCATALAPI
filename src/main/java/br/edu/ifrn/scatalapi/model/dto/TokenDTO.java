package br.edu.ifrn.scatalapi.model.dto;

import java.io.Serializable;

import br.edu.ifrn.suapi.ClienteSUAP;
import br.edu.ifrn.suapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;
import br.edu.ifrn.suapi.exception.TokenInvalidoException;
import br.edu.ifrn.suapi.model.AlunoSUAP;
import lombok.Getter;

public class TokenDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter private final String conteudo;
	@Getter private final boolean valido;
	
	private transient ClienteSUAP clienteSUAP;
	
	public TokenDTO(CredenciaisDTO credenciais) throws FalhaAoConectarComSUAPException {
		String matricula = credenciais.getMatricula();
		String senha = credenciais.getSenha();
		
		String conteudo = null;
		boolean isValido = false;

		try {
			ClienteSUAP cliente = new ClienteSUAP(matricula, senha);
			conteudo = cliente.getTOKEN();
			isValido = true;
			this.clienteSUAP = cliente;
		} catch (CredenciaisIncorretasException e) {
			conteudo = e.getMessage();
		} catch (FalhaAoConectarComSUAPException e) {
			conteudo = e.getMessage();
			throw e;
		}
		
		this.valido = isValido;
		this.conteudo = conteudo;
	}
	
	public TokenDTO(String token) throws FalhaAoConectarComSUAPException {
		String conteudo = token;
		boolean isValido = false;
		
		ClienteSUAP clienteSUAP = null;
		try {
			clienteSUAP = new ClienteSUAP(token);
			isValido = true;
		} catch (TokenInvalidoException e) {
			conteudo = e.getMessage();
		} catch (FalhaAoConectarComSUAPException e) {
			conteudo = e.getMessage();
			throw e;
		}
		
		this.clienteSUAP = clienteSUAP;
		this.conteudo = conteudo;
		this.valido = isValido;
	}

	public AlunoSUAP asAluno() {
		return this.clienteSUAP.getUsuario(AlunoSUAP.class);
	}
}
