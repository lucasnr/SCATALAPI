package br.edu.ifrn.scatalapi.model;

import br.edu.ifrn.suapi.ClienteSUAP;
import br.edu.ifrn.suapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;
import br.edu.ifrn.suapi.exception.TokenInvalidoException;
import br.edu.ifrn.suapi.model.UsuarioSUAP;
import lombok.Getter;

public class Token {

	@Getter private final String conteudo;
	@Getter private final boolean valido;
	
	private transient ClienteSUAP clienteSUAP;
	
	public Token(Credenciais aluno) {
		String matricula = aluno.getMatricula();
		String senha = aluno.getSenha();
		
		String conteudo = null;
		boolean isValido = false;

		try {
			ClienteSUAP cliente = new ClienteSUAP(matricula, senha);
			conteudo = cliente.getTOKEN();
			isValido = true;
			this.clienteSUAP = cliente;
		} catch (FalhaAoConectarComSUAPException | CredenciaisIncorretasException e) {
			conteudo = e.getMessage();
		}
		
		this.valido = isValido;
		this.conteudo = conteudo;
	}
	
	public Token(String token) {
		String conteudo = token;
		boolean isValido = true;
		
		ClienteSUAP clienteSUAP = null;
		try {
			clienteSUAP = new ClienteSUAP(token);
		} catch (TokenInvalidoException | FalhaAoConectarComSUAPException e) {
			conteudo = e.getMessage();
			isValido = false;
		}
		
		this.clienteSUAP = clienteSUAP;
		this.conteudo = conteudo;
		this.valido = isValido;
	}

	public <T extends UsuarioSUAP> T getUsuario(Class<T> clazz) {
		return this.clienteSUAP.getUsuario(clazz);
	}
}
