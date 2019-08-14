package br.edu.ifrn.scatalapi.advice;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.edu.ifrn.suapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.exception.TokenInvalidoException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(value = {TokenInvalidoException.class, CredenciaisIncorretasException.class})
	public void Handle(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
