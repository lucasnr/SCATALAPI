package br.edu.ifrn.scatalapi.model;

import br.edu.ifrn.suapi.ClienteSUAP;
import br.edu.ifrn.suapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;
import br.edu.ifrn.suapi.model.UsuarioSUAP;

public class Token {

	private final String conteudo;
	private final boolean valido;
	
	public Token(AlunoLoginDTO aluno) {
		String matricula = aluno.getMatricula();
		String senha = aluno.getSenha();
		
		String conteudo = null;
		boolean isValido = false;

		try {
			ClienteSUAP cliente = new ClienteSUAP(matricula, senha);
			conteudo = cliente.getTOKEN();
			isValido = true;
		} catch (FalhaAoConectarComSUAPException | CredenciaisIncorretasException e) {
			conteudo = e.getMessage();
		}
		
		this.valido = isValido;
		this.conteudo = conteudo;
	}

	public String getToken() {
		return conteudo;
	}
	
	public boolean isValido() {
		return valido;
	}
	
	public <T extends UsuarioSUAP> T getUsuario(Class<T> clazz) {
		ClienteSUAP cliente = new ClienteSUAP(this.conteudo);
		return cliente.getUsuario(clazz);
	}
}
