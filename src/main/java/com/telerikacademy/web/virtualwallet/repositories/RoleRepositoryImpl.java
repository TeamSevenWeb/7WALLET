package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Role;
import com.telerikacademy.web.virtualwallet.repositories.contracts.RoleRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends AbstractCRUDRepository<Role> implements RoleRepository {
    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        super(Role.class, sessionFactory);
    }
}
