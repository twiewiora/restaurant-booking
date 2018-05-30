package com.application.restaurantBooking.jwt.jwtFactory;

import com.application.restaurantBooking.jwt.jwtModel.JwtRestorer;
import com.application.restaurantBooking.persistence.model.Restorer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.application.restaurantBooking.persistence.model.Authority;

import java.util.List;
import java.util.stream.Collectors;

public final class JwtRestorerFactory {

    private JwtRestorerFactory() {
    }

    public static JwtRestorer create(Restorer restorer) {
        return new JwtRestorer(
                restorer.getId(),
                restorer.getUsername(),
                restorer.getPassword(),
                mapToGrantedAuthorities(restorer.getAuthorities()),
                restorer.getEnabled()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
