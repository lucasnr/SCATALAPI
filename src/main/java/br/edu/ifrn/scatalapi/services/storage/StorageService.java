package br.edu.ifrn.scatalapi.services.storage;

import org.springframework.stereotype.Service;

@Service
public interface StorageService {

	public String getLinkById(String id);

	public String upload(Imagem imagem);
}
