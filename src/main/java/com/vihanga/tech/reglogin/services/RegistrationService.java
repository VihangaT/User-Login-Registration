package com.vihanga.tech.reglogin.services;

import com.vihanga.tech.reglogin.appuser.AppUser;
import com.vihanga.tech.reglogin.appuser.AppUserRole;
import com.vihanga.tech.reglogin.appuser.RegistrationRequest;
import com.vihanga.tech.reglogin.registration.EmailValidator;
import com.vihanga.tech.reglogin.registration.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {


    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private UserServices userServices;
    @Autowired
    private TokenConfirmationService tokenConfirmationService;

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

    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = tokenConfirmationService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token Expired");
        }

        int i = tokenConfirmationService.setConfirmedAT(token);

        userServices.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }
}
