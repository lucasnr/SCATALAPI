package br.edu.ifrn.scatalapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface StorageService {

	public String store(MultipartFile file, String matricula) throws IOException;
}
