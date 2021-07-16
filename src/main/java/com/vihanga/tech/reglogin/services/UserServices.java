package com.vihanga.tech.reglogin.services;

import com.vihanga.tech.reglogin.appuser.AppUser;
import com.vihanga.tech.reglogin.email.EmailSender;
import com.vihanga.tech.reglogin.registration.token.ConfirmationToken;
import com.vihanga.tech.reglogin.repositories.UserRepositories;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserServices implements UserDetailsService {


    private static String userNotFound = "user with email %s not found";
    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenConfirmationService tokenConfirmationService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private RegistrationService registrationService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepositories.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(String.format(userNotFound, s)));
    }

    public String signUoUser(AppUser appUser) {
        Optional<AppUser> userExits = userRepositories.findByEmail(appUser.getUsername());
        if (userExits.equals(appUser)) {
            boolean checkEnable = userRepositories.enableCheck(appUser.getEmail());
            if (!checkEnable) {
                String newToken = tokenGenerator(appUser);
                String link = "http://localhost:8081/api/v1/registration/confirm?token=" + newToken;
                emailSender.send(appUser.getEmail(), registrationService.buildEmail(appUser.getFirstName(), link));
            } else {
                throw new IllegalStateException("Email already taken");
            }
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        userRepositories.save(appUser);

        return tokenGenerator(appUser);

    }

    private String tokenGenerator(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusHours(2), appUser);

        tokenConfirmationService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAppUser(String email) {
        return userRepositories.enableAppUser(email);
    }

}
