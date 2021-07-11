package com.vihanga.tech.reglogin.services;

import com.vihanga.tech.reglogin.registration.token.ConfirmationToken;
import com.vihanga.tech.reglogin.repositories.TokenConfirmationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TokenConfirmationService {
    @Autowired
    private TokenConfirmationRepository tokenConfirmationRepository;

    @Autowired
    private TokenConfirmationRepository confirmationTokenRepository;

    public int setConfirmedAT(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        tokenConfirmationRepository.save(token);
    }
}
