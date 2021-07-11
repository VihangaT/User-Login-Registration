package com.vihanga.tech.reglogin.services;

import com.vihanga.tech.reglogin.appuser.AppUser;
import com.vihanga.tech.reglogin.registration.token.ConfirmationToken;
import com.vihanga.tech.reglogin.repositories.UserRepositories;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserServices implements UserDetailsService {


    private static String userNotFound = "user with email %s not found";
    @Autowired
    private UserRepositories userRepositories;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenConfirmationService tokenConfirmationService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepositories.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(String.format(userNotFound, s)));
    }

    public String signUoUser(AppUser appUser) {
        boolean userExits = userRepositories.findByEmail(appUser.getUsername()).isPresent();
        if (userExits) {
            throw new IllegalStateException("Email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        userRepositories.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusHours(2), appUser);

        tokenConfirmationService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return userRepositories.enableAppUser(email);
    }

}
