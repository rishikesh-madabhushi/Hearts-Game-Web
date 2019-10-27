package com.llwantedll.webhearts.models.dtolayer.converter;

import com.llwantedll.webhearts.models.dtolayer.wrappers.RoleWrapper;
import com.llwantedll.webhearts.models.dtolayer.wrappers.UserDetailsWrapper;
import com.llwantedll.webhearts.models.entities.Role;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDetailsDTOConverter implements DTOConverter<User, UserDetailsWrapper> {

    private final UserService userService;

    private final DTOConverter<Role, RoleWrapper> roleDTOConverter;

    @Autowired
    public UserDetailsDTOConverter(UserService userService,
                                   DTOConverter<Role, RoleWrapper> roleDTOConverter) {
        this.userService = userService;
        this.roleDTOConverter = roleDTOConverter;
    }

    @Override
    public User forward(UserDetailsWrapper dto) {
        return userService.getByLogin(dto.getLogin());
    }

    @Override
    public UserDetailsWrapper backward(User entity) {
        UserDetailsWrapper userDetailsWrapper = new UserDetailsWrapper();

        userDetailsWrapper.setEmail(entity.getEmail());
        userDetailsWrapper.setLogin(entity.getLogin());
        userDetailsWrapper.setPassword(entity.getPassword());

        Set<Role> roles = entity.getRoles();

        Set<RoleWrapper> roleWrappers = roles
                .stream()
                .map(roleDTOConverter::backward)
                .collect(Collectors.toSet());

        userDetailsWrapper.setRoles(roleWrappers);

        return userDetailsWrapper;
    }
}
