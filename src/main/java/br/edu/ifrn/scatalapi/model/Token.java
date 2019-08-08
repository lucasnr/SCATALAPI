package br.edu.ifrn.scatalapi.model;

import java.io.Serializable;

import br.edu.ifrn.scatalapi.dto.CredenciaisDTO;
import br.edu.ifrn.suapi.ClienteSUAP;
import br.edu.ifrn.suapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;
import br.edu.ifrn.suapi.exception.TokenInvalidoException;
import br.edu.ifrn.suapi.model.AlunoSUAP;
import lombok.Getter;

public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Getter private final String token;
	
	private transient ClienteSUAP clienteSUAP;
	
	public Token(CredenciaisDTO credenciais) throws FalhaAoConectarComSUAPException, CredenciaisIncorretasException {
		this.clienteSUAP = new ClienteSUAP(credenciais.getMatricula(), credenciais.getSenha());
		this.token = clienteSUAP.getTOKEN();
	}
	
	public Token(String token) throws FalhaAoConectarComSUAPException, TokenInvalidoException {
		this.clienteSUAP = new ClienteSUAP(token);
		this.token = clienteSUAP.getTOKEN();
	}

	public AlunoSUAP asAluno() {
		return this.clienteSUAP.getUsuario(AlunoSUAP.class);
	}
}
