package com.dircomercio.site_backend.auth.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
    List<Token> findAllByUserIdAndExpiredFalseAndRevokedFalse(Long userId);

    Token findByToken(String jwtToken);
}
