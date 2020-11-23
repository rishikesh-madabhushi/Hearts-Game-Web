package com.llwantedll.webhearts.models.configs.security;

import com.llwantedll.webhearts.models.dtolayer.wrappers.UserDetailsWrapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//THIS CLASS GIVES CUSTOM PROPERTIES FOR USER PRINCIPAL
public class CustomUserDetails implements UserDetails {

    private final UserDetailsWrapper user;

    public CustomUserDetails(UserDetailsWrapper user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles()
                .forEach(e -> grantedAuthorities
                .add(new SimpleGrantedAuthority(e.getKey())));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDetailsWrapper getUser() {
        return user;
    }
}
