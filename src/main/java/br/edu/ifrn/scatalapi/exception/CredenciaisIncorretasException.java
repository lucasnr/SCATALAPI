package br.edu.ifrn.scatalapi.exception;

public class CredenciaisIncorretasException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "As credenciais informadas estão incorretas";
	}
}
