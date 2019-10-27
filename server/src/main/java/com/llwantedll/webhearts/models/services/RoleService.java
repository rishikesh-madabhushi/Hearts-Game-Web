package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.entities.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    Role getAdministratorRole();

    Role getUserRole();

    Role getByKey(String key);
}
