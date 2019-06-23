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
    	FilterRegistrationBean<AutenticacaoFilter> filterRegistrationBean = new FilterRegistrationBean<>();
    	filterRegistrationBean.setFilter(new AutenticacaoFilter());
    	filterRegistrationBean.addUrlPatterns("/*");
    	
    	return filterRegistrationBean;
    }
}
