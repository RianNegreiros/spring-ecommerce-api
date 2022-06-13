package com.riannegreiros.springecommerce.modules.customer.token.repository;

import com.riannegreiros.springecommerce.modules.customer.token.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<ConfirmationToken, Long> {
    @Query("SELECT c FROM ConfirmationToken c WHERE c.token = ?1")
    Optional<ConfirmationToken> findByConfirmationToken(String token);

    @Query("UPDATE ConfirmationToken c SET c.confirmedTime = ?2 WHERE c.token = ?1")
    @Modifying
    void updateConfirmedTime(String token, Date confirmedTime);
}
