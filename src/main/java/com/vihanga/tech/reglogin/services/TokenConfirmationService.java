package com.vihanga.tech.reglogin.services;

import com.vihanga.tech.reglogin.registration.token.ConfirmationToken;
import com.vihanga.tech.reglogin.repositories.TokenConfirmationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TokenConfirmationService {
    @Autowired
    private TokenConfirmationRepository tokenConfirmationRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        tokenConfirmationRepository.save(token);
    }
}
