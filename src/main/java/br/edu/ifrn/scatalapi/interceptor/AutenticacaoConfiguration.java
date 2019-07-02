package br.edu.ifrn.scatalapi.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AutenticacaoConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AutenticacaoInterceptor());
	}
}
