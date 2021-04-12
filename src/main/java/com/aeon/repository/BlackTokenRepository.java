package com.aeon.repository;

import com.aeon.domain.BlackToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackTokenRepository extends JpaRepository<BlackToken, Long> {

    boolean existsAllByToken(String token);
}
