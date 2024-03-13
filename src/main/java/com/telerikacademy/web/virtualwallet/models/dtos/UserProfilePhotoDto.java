package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserProfilePhotoDto {

    @NotNull
    @Pattern(regexp = "(\\S+(\\.(?i)(jpg|png|bmp))$)", message = "Invalid image format; only JPG, PNG, and BMP formats are allowed.")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
