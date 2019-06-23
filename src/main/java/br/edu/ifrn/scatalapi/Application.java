package br.edu.ifrn.scatalapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import br.edu.ifrn.scatalapi.filter.AutenticacaoFilter;

@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }
  
    @Bean
    public FilterRegistrationBean<AutenticacaoFilter> filter(){
    	FilterRegistrationBean<AutenticacaoFilter> registradorDeFiltro = new FilterRegistrationBean<>();
    	registradorDeFiltro.setFilter(new AutenticacaoFilter());
    	registradorDeFiltro.addUrlPatterns("/*");
    	
    	return registradorDeFiltro;
    }
}
