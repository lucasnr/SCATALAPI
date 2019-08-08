package br.edu.ifrn.scatalapi.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

@ApiResponses(@ApiResponse(code = 401, message = "O header 'Authorization' n�o foi informado ou n�o � v�lido"))
public @interface AutenticadoRequired {

}
