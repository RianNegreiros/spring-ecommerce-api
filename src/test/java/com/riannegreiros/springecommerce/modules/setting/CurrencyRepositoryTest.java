package com.riannegreiros.springecommerce.modules.setting;

import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CurrencyRepositoryTest {

    @Autowired
    CurrencyRepository currencyRepository;

    @Test
    public void testCreateCurrency() {
        Currency currency = new Currency("United States Dollar", "$", "USD");

        Currency savedCurrency = currencyRepository.save(currency);

        assertThat(savedCurrency).isNotNull();
        assertThat(savedCurrency).isEqualTo(currency);
    }
}
