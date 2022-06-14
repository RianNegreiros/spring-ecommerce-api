package com.riannegreiros.springecommerce.AWS;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import static com.riannegreiros.springecommerce.utils.AWSConstants.BUCKET_NAME;

@Service
public class StorageService {
    private final AmazonS3 s3Client;

    public StorageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String fileName, MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartFile(multipartFile);
        s3Client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file));
    }

    public byte[] downloadFile(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(BUCKET_NAME, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public void deleteFile(String fileName) {
        s3Client.deleteObject(BUCKET_NAME, fileName);
    }

    private File convertMultiPartFile(MultipartFile multipartFile) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        try (OutputStream os = new FileOutputStream(convertedFile)) {
            os.write(multipartFile.getBytes());
        }
        return convertedFile;
    }
}
