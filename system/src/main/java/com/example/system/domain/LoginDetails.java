package com.example.system.domain;

import com.example.system.domain.SysUsers;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Classname LoginDetails
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/19 16:27
 * @Created by 16537
 */
@Data
public class LoginDetails implements UserDetails, Serializable {
    private SysUsers sysUsers;
    private List<String> permission;
    private String token;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return sysUsers.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUsers.getUsername();
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
}
