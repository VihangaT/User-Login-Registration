package com.vihanga.tech.reglogin.services;

import com.vihanga.tech.reglogin.appuser.AppUser;
import com.vihanga.tech.reglogin.appuser.AppUserRole;
import com.vihanga.tech.reglogin.appuser.RegistrationRequest;
import com.vihanga.tech.reglogin.registration.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {


    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private UserServices userServices;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email is not valid");
        }
        return userServices.signUoUser(new AppUser(request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }
}
