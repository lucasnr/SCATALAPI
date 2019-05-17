package br.edu.ifrn.scatalapi.model;

import br.edu.ifrn.suapi.ClienteSUAP;
import br.edu.ifrn.suapi.exception.FalhaAoConectarComSUAPException;

public class AutenticacaoToken {

	private String token;
	
	public AutenticacaoToken(AlunoLoginDTO aluno) {
		String matricula = aluno.getMatricula();
		String senha = aluno.getSenha();
		
		try {
			new ClienteSUAP(matricula, senha);
		} catch (FalhaAoConectarComSUAPException e) {
			e.printStackTrace();
		}
	}
	
	public String getToken() {
		return token;
	}
}
