package com.telerikacademy.web.virtualwallet.repositories.contracts;

import com.telerikacademy.web.virtualwallet.filters.UserFilterOptions;
import com.telerikacademy.web.virtualwallet.models.User;

import java.util.List;

public interface UserRepository extends BaseCRUDRepository<User>{

    User searchByAnyMatch(String parameter);

    List<User> getAllUsersFiltered(UserFilterOptions filterOptions);
}
