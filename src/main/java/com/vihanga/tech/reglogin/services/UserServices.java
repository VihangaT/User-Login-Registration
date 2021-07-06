package com.vihanga.tech.reglogin.services;

import com.vihanga.tech.reglogin.repositories.UserRepositories;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServices implements UserDetailsService {


    private static String userNotFound = "user with email %s not found";
    @Autowired
    private UserRepositories userRepositories;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepositories.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(String.format(userNotFound, s)));
    }
}
