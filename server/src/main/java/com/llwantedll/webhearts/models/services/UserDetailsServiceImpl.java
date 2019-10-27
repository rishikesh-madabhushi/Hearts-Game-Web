package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.configs.security.CustomUserDetails;
import com.llwantedll.webhearts.models.dtolayer.converter.DTOConverter;
import com.llwantedll.webhearts.models.dtolayer.wrappers.UserDetailsWrapper;
import com.llwantedll.webhearts.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    private final DTOConverter<User, UserDetailsWrapper> userDTOConverter;

    @Autowired
    public UserDetailsServiceImpl(UserService userService,
                                  DTOConverter<User, UserDetailsWrapper> userDTOConverter) {
        this.userService = userService;
        this.userDTOConverter = userDTOConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userService.getByLogin(username);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(userDTOConverter.backward(user));
    }
}