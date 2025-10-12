package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.entities.Role;
import com.dawanproject.booktracker.entities.User;

import java.util.List;

public interface AccountService {

    Role addNewRole(Role role);

    void addRoleToUser(User user, List<Role> roles);
}
