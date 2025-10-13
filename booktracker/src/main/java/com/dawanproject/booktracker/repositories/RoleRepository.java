package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.Role;
import com.dawanproject.booktracker.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(RoleName roleName);
}
