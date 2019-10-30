package br.edu.ifrn.scatalapi;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import br.edu.ifrn.scatalapi.advice.GlobalExceptionHandler;
import br.edu.ifrn.scatalapi.config.WebCorsConfig;
import br.edu.ifrn.scatalapi.controller.AlunoController;
import br.edu.ifrn.scatalapi.interceptor.AutenticacaoInterceptor;
import br.edu.ifrn.scatalapi.service.impl.AlunoServiceImpl;
import br.edu.ifrn.scatalapi.services.storage.GoogleDriveStorageService;
import br.edu.ifrn.scatalapi.services.storage.StorageService;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
@ComponentScan(basePackageClasses = {AlunoController.class, AlunoServiceImpl.class, AutenticacaoInterceptor.class, WebCorsConfig.class, GlobalExceptionHandler.class})
public class Application {
	
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }

    @Bean
    public StorageService storageService() {
    	return new GoogleDriveStorageService();
    }
    
    @Bean
    public Docket swaggerDocket() {
    	return new Docket(DocumentationType.SWAGGER_2)
    			.select()
    			.apis(RequestHandlerSelectors.basePackage("br.edu.ifrn.scatalapi.controller"))
    			.paths(PathSelectors.ant("/**"))
    			.build()
    			.apiInfo(info())
    			.globalOperationParameters(
					Arrays.asList(
    					new ParameterBuilder()
			                .name("Authorization")
			                .description("Header de Autorização")
			                .modelRef(new ModelRef("string"))
			                .parameterType("header")
			                .required(false)
			                .build()
	                )
				);
    }
    
    private ApiInfo info() {
		return new ApiInfoBuilder()
			.title("SCATAL Spring Boot RESTful API")
			.description("Spring Boot RESTful API para o Sistema de Comunicação Aluno-Tutor de Aprendizagem e Laboratório")
			.version("1.0")
			.termsOfServiceUrl("Terms of Service")
			.contact(new Contact("Lucas do Nascimento", "https://lucasnr.github.io/", "lucasnascimentoribeiro13@gmail.com"))
			.license("Apache License Version 2.0")
			.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
			.build();
    }
}
