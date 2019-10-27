package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.configs.security.UserRole;
import com.llwantedll.webhearts.models.entities.Role;
import com.llwantedll.webhearts.models.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getAdministratorRole() {
        Role adminRole = roleRepository.getByKey(UserRole.ADMIN.name());

        if (Objects.isNull(adminRole)) {
            logger.error("No Admin Role was found in the database! Database must be updated!");
        }

        return adminRole;
    }

    @Override
    public Role getUserRole() {
        Role userRole = roleRepository.getByKey(UserRole.USER.name());

        if (Objects.isNull(userRole)) {
            logger.error("No User Role was found in the database! Database must be updated!");
        }

        return userRole;
    }

    @Override
    public Role getByKey(String key) {
        return roleRepository.getByKey(key);
    }
}
