package com.llwantedll.webhearts.models.services;

import com.llwantedll.webhearts.models.dtolayer.wrappers.UserForm;
import com.llwantedll.webhearts.models.entities.User;
import com.llwantedll.webhearts.models.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(UserForm userForm) {
        User user = new User();

        String login = userForm.getLogin();
        String password = passwordEncoder.encode(userForm.getPassword());
        String email = userForm.getEmail();

        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(roleService.getUserRole());
        return userRepository.save(user);
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isLoginExists(String login) {
        return userRepository.countByLogin(login) > 0;
    }
}
