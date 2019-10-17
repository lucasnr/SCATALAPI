package br.edu.ifrn.scatalapi.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
		@ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "O indice da p�gina", defaultValue = "0"),
		@ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "O n�mero de registros por p�gina", defaultValue = "10"),
		@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Criteria de ordena��o no padr�o: propriedade(,asc|desc)") })
public @interface ApiPageable {

}
