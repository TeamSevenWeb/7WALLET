package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.ProfilePhotoRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProfilePhotoRepositoryImpl extends AbstractCRUDRepository<ProfilePhoto> implements ProfilePhotoRepository {
    @Autowired
    public ProfilePhotoRepositoryImpl(SessionFactory sessionFactory) {
        super(ProfilePhoto.class, sessionFactory);
    }
}
