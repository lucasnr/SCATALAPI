package br.edu.ifrn.scatalapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.edu.ifrn.scatalapi.exception.TokenInvalidoException;
import br.edu.ifrn.scatalapi.exception.TokenNaoInformadoException;
import br.edu.ifrn.scatalapi.model.Token;

public class AutenticacaoInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HandlerMethod handlerMethod;
		try {
			handlerMethod = (HandlerMethod) handler;
		} catch (Exception e) {
			return true;
		}
		
		boolean isClasseProtegida = handlerMethod.getMethod().getDeclaringClass().isAnnotationPresent(AutenticadoRequired.class);
		boolean isMetodoProtegido = handlerMethod.getMethod().isAnnotationPresent(AutenticadoRequired.class);

		if (isClasseProtegida || isMetodoProtegido) {
			String token = request.getHeader("Authorization");

			if (token == null)
				throw new TokenNaoInformadoException();

			if (!new Token(token).isValido())
				throw new TokenInvalidoException();

			return true;
		}

		return true;
	}

}