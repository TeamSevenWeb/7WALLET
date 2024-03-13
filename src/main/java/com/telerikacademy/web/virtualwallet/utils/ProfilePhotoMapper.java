package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserProfilePhotoDto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.ProfilePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfilePhotoMapper {

    private final ProfilePhotoRepository profilePhotoRepository;

    @Autowired
    public ProfilePhotoMapper(ProfilePhotoRepository profilePhotoRepository) {
        this.profilePhotoRepository = profilePhotoRepository;
    }

    public ProfilePhoto fromDto(int id, UserProfilePhotoDto userProfilePhotoDto) {
        ProfilePhoto profilePhoto = fromDto(userProfilePhotoDto);
        profilePhoto.setProfilePhotoId(id);
        profilePhoto.setUser(profilePhotoRepository.getById(id).getUser());
        return profilePhoto;

    }

    public ProfilePhoto fromDto(UserProfilePhotoDto userProfilePhotoDto) {
        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setProfilePhoto(userProfilePhotoDto.getUrl());
        return profilePhoto;
    }

}
