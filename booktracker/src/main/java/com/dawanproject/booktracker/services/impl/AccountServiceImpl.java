package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.entities.Role;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.repositories.RoleRepository;
import com.dawanproject.booktracker.repositories.UserRepository;
import com.dawanproject.booktracker.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }

    @Override
    public Role addNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(User user, List<Role> roles) {
        User userFromDb = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        roles.stream()
                .map(Role::getRoleName)
                .map(roleRepository::findByRoleName)
                .forEach(userFromDb.getRoles()::add);
    }
}
