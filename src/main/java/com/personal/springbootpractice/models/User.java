package com.personal.springbootpractice.models;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Document
@ToString
public class User implements UserDetails {

    @Id
    private String username;
    @Indexed(unique = true)
    private String password;
    private String schoolName;

    private boolean active = true;
    private Set<GrantedAuthority> roles = new HashSet<>();

    @Builder
    public User(String username, String password, String schoolName) {
        this.username = username;
        this.password = password;
        this.schoolName = schoolName;
        this.roles.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public User() {
        this.roles.add(new SimpleGrantedAuthority("ROLE_USER"));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
