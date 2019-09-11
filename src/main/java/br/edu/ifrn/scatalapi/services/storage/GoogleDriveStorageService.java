package br.edu.ifrn.scatalapi.services.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class GoogleDriveStorageService implements StorageService {

	@Override
	public String getLinkById(String id) {
		try {
			return getLinkFromDriveById(id);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String upload(Imagem imagem) {
		try {
			String link = uploadImage(imagem);
			return link;
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static final String APPLICATION_NAME = "SCATAL";

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleDriveStorageService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static String getLinkFromDriveById(String id) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        Drive service = getService();
        File file = service.files().get(id).setFields("id, webContentLink").execute();
        return file.getWebContentLink();
    }
    
    private static Drive getService() throws GeneralSecurityException, IOException {
    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();		
	}

	private static String uploadImage(Imagem imagem) throws IOException, GeneralSecurityException {
		final String imageFolderId = "1dArf-8BT_7l8C9bq4CjccNFH-2n5S5yp";
    	
    	File fileMetadata = new File();
    	fileMetadata.setName("imagem");
		fileMetadata.setParents(Collections.singletonList(imageFolderId));

    	java.io.File filePath = imagem.getFile();
    	FileContent mediaContent = new FileContent("image/png", filePath);
    	
    	Drive service = getService();
    	File file = service.files().create(fileMetadata, mediaContent)
    	    .setFields("id, webContentLink")
    	    .execute();
    	
    	imagem.getFile().delete(); // deletes the temp file once its uploaded
    	return file.getWebContentLink();
	}
}

