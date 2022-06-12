package com.riannegreiros.springecommerce.modules.customer.token.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.customer.service.CustomerService;
import com.riannegreiros.springecommerce.modules.customer.token.entity.ConfirmationToken;
import com.riannegreiros.springecommerce.modules.customer.token.repository.TokenRepository;
import com.riannegreiros.springecommerce.modules.customer.token.service.TokenService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    private final CustomerService customerService;

    public TokenServiceImpl(TokenRepository tokenRepository, CustomerService customerService) {
        this.tokenRepository = tokenRepository;
        this.customerService = customerService;
    }

    @Override
    public void save(ConfirmationToken confirmationToken) {
        tokenRepository.save(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return tokenRepository.findByConfirmationToken(token);
    }

    @Override
    public void setConfirmed(String token) {
        tokenRepository.updateConfirmedTime(token, new Date());
    }

    public void confirmToken(String token) {
        ConfirmationToken tokenExist = tokenRepository.findByConfirmationToken(token).orElseThrow(() -> new ResourceNotFoundException("Confirmation Token", "Token", token));

        if (tokenExist.getConfirmedTime() != null) throw new Error("Email already confirmed");

        Date expireTime = tokenExist.getExpiresTime();

        if (expireTime.before(new Date())) throw new Error("Token expired");

        setConfirmed(token);
        customerService.enable(tokenExist.getCustomer().getId());
    }
}
