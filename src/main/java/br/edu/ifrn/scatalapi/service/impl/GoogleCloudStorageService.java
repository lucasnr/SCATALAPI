package br.edu.ifrn.scatalapi.service.impl;

import br.edu.ifrn.scatalapi.service.StorageService;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class GoogleCloudStorageService implements StorageService {

    private static Storage storage = StorageOptions.getDefaultInstance().getService();

    @Override
    public String store(MultipartFile file, String matricula) throws IOException {
        BlobInfo info = BlobInfo.newBuilder("imagens-de-usuarios",
                String.format("%s.jpg", matricula)).build();

        BlobInfo blobInfo = storage.create(info, file.getBytes(),
                Storage.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ));
        return blobInfo.getMediaLink();
    }
}
