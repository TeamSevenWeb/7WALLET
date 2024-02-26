package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.dtos.ProfilePhotoDto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.ProfilePhotoRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfilePhotoMapper {

    private final ProfilePhotoRepository profilePhotoRepository;

    @Autowired
    public ProfilePhotoMapper(ProfilePhotoRepository profilePhotoRepository) {
        this.profilePhotoRepository = profilePhotoRepository;
    }

    public ProfilePhoto fromDto(int id, ProfilePhotoDto profilePhotoDto) {
        ProfilePhoto profilePhoto = fromDto(profilePhotoDto);
        profilePhoto.setProfilePhotoId(id);
        profilePhoto.setUser(profilePhotoRepository.getById(id).getUser());
        return profilePhoto;

    }

    public ProfilePhoto fromDto(ProfilePhotoDto profilePhotoDto) {
        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setProfilePhoto(profilePhotoDto.getUrl());
        return profilePhoto;
    }

}
