package br.edu.ifrn.scatalapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import br.edu.ifrn.scatalapi.controller.TutoriaController;
import br.edu.ifrn.scatalapi.interceptor.AutenticacaoInterceptor;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
@ComponentScan(basePackageClasses = {TutoriaController.class, AutenticacaoInterceptor.class})
public class Application {
	
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public Docket swaggerDocket() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.select()
    			.apis(RequestHandlerSelectors.basePackage("br.edu.ifrn.scatalapi.controller"))
    			.paths(PathSelectors.ant("/**"))
    			.build();
    }
}
