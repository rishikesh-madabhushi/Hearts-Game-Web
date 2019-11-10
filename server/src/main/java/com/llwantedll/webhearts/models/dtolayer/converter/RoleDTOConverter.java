package com.llwantedll.webhearts.models.dtolayer.converter;

import com.llwantedll.webhearts.models.dtolayer.wrappers.RoleWrapper;
import com.llwantedll.webhearts.models.entities.Role;
import com.llwantedll.webhearts.models.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RoleDTOConverter implements DTOConverter<Role, RoleWrapper> {

    private final RoleService roleService;

    @Autowired
    public RoleDTOConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role forward(RoleWrapper dto) {
        return roleService.getByKey(dto.getKey());
    }

    @Override
    public RoleWrapper backward(Role entity) {
        if(Objects.isNull(entity)){
            return null;
        }
        return new RoleWrapper(entity.getKey());
    }
}
