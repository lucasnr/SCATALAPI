package br.edu.ifrn.scatalapi.advice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.edu.ifrn.suapi.exception.CredenciaisIncorretasException;
import br.edu.ifrn.suapi.exception.TokenInvalidoException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String FILE_SIZE_LIMIT_ERROR_MESSAGE = "O tamanho do arquivo não pode ser maior que 5MB";

	@ExceptionHandler(value = { TokenInvalidoException.class, CredenciaisIncorretasException.class })
	public void handleSuapExceptions(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> handleFileSizeLimitExceededException(
			MaxUploadSizeExceededException exc,
			HttpServletRequest request, 
			HttpServletResponse response
	) {
		Map<String, Object> body = initBadRequestBody();
		body.put("message", FILE_SIZE_LIMIT_ERROR_MESSAGE);
		return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = initBadRequestBody();
		List<ValidationError> errors = ex.getBindingResult().getFieldErrors().stream().map(ValidationError::new)
				.collect(Collectors.toList());

		body.put("errors", errors);
		return new ResponseEntity<>(body, headers, status);
	}
	
	private Map<String, Object> initBadRequestBody() {
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
		return body;
	}

}
