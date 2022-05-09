package com.deenn.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
public class CloudinaryServiceImpl implements CloudinaryService {


    private final Cloudinary cloudinaryConfig;

    public CloudinaryServiceImpl(Cloudinary cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }


    @Override
    public String uploadFile(MultipartFile image) {
        try {

            File uploadFile = convertMultiPartToFile(image);
            var uploadResult = cloudinaryConfig.uploader().upload(uploadFile, ObjectUtils.emptyMap());
            boolean isDeleted = uploadFile.delete();
            if (isDeleted) {
                System.out.println("File successfully deleted");
            } else
                System.out.println("File doesn't exist");
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile image) throws IOException {

        String file = image.getOriginalFilename();
        assert file != null;
        File convertFile = new File(file);
        FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
        fileOutputStream.write(image.getBytes());
        fileOutputStream.close();
        return convertFile;

    }
}
