package br.edu.ifrn.scatalapi.interceptor;

import java.util.HashSet;
import java.util.Set;

public class ChaveSecretaSample {

	private static Set<String> chaves = new HashSet<>();
	
	static {
		chaves.add("YOUR KEY");
	}
	
	public static boolean is(String chave) {
		return chaves.contains(chave);
	}

	
}
