package br.edu.ifrn.scatalapi.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

public class FileTypeValidator {

	private final Set<String> validTypes;

	public FileTypeValidator() {
		validTypes = new HashSet<>();
		validTypes.add("image/jpeg");
		validTypes.add("image/jpg");
		validTypes.add("image/png");
	}
	
	public boolean valid(MultipartFile file) {
		return validTypes.contains(file.getContentType());
	}

}
