package br.edu.ifrn.scatalapi.model;

import br.edu.ifrn.scatalapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.ClienteSUAP;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;

public class Token {

	private String token;
	
	public Token(AlunoLoginDTO aluno) throws CredenciaisIncorretasException {
		String matricula = aluno.getMatricula();
		String senha = aluno.getSenha();
		
		try {
			ClienteSUAP cliente = new ClienteSUAP(matricula, senha);
			if(! cliente.isAutenticado()) {
				throw new CredenciaisIncorretasException();
			}
			this.token = cliente.getTOKEN();
		} catch (FalhaAoConectarComSUAPException e) {
			e.printStackTrace();
		}
	}
	
	public String getToken() {
		return token;
	}
}
