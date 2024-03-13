package com.telerikacademy.web.virtualwallet.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.telerikacademy.web.virtualwallet.exceptions.InvalidFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryHelper {
    private final Cloudinary cloudinary;

    public CloudinaryHelper(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String cloudinaryUpload(MultipartFile file) throws IOException {
        Map upload;
        try {
            upload = cloudinary.uploader()
                    .upload(file.getBytes()
                            , ObjectUtils.asMap("resource_type", "auto","allowed_formats", new String[]{"jpg", "png"}));
        } catch (RuntimeException e) {
            throw new InvalidFileException(e.getMessage());
        }
        String url = (String) upload.get("url");

        return url;}

}


