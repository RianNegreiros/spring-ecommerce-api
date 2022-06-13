package com.riannegreiros.springecommerce.modules.customer.token.service;

import com.riannegreiros.springecommerce.modules.customer.token.entity.ConfirmationToken;
import java.util.Optional;

public interface TokenService {

    void save(ConfirmationToken confirmationToken);
    Optional<ConfirmationToken> getToken(String token);
    void setConfirmed(String token);
}
