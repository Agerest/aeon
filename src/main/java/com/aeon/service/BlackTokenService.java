package com.aeon.service;

import com.aeon.domain.BlackToken;
import com.aeon.repository.BlackTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlackTokenService {

    private final BlackTokenRepository blackTokenRepository;

    @Transactional(readOnly = true)
    public boolean existsInBlackList(String token) {
        return blackTokenRepository.existsAllByToken(token);
    }

    @Transactional
    public void addNewBlackToken(String token) {
        blackTokenRepository.save(new BlackToken(token));
    }
}
