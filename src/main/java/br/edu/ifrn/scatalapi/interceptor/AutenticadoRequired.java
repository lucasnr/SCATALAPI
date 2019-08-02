package br.edu.ifrn.scatalapi.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

@ApiResponses(@ApiResponse(code = 401, message = "Voc� n�o tem permiss�o para acessar esse recurso ou n�o informou o token de Autoriza��o"))
public @interface AutenticadoRequired {

}
