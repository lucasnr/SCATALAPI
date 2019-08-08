package br.edu.ifrn.scatalapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import br.edu.ifrn.scatalapi.exception.ChaveInvalidaException;
import br.edu.ifrn.scatalapi.exception.HeaderAuthorizationNaoInformadoException;

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
			String authorization = request.getHeader("Authorization");

			if (StringUtils.isEmpty(authorization))
				throw new HeaderAuthorizationNaoInformadoException();

			boolean naoAutorizado = ! ChaveSecreta.is(authorization);
			if(naoAutorizado)
				throw new ChaveInvalidaException();
		}

		return true;
	}

}