package com.riannegreiros.springecommerce.modules.setting.currency;

import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

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

    @Test
    public void testUpdate() {
        Currency currency = new Currency("Dollar", "$", "USD");
        Currency savedCurrency = currencyRepository.save(currency);

        savedCurrency.setName("United States Dollar");
        Currency updatedCurrency = currencyRepository.save(savedCurrency);

        assertThat(updatedCurrency.getName()).isEqualTo(savedCurrency.getName());
    }

    @Test
    public void testDelete() {
        Currency currency = new Currency("Dollar", "$", "USD");
        Currency savedCurrency = currencyRepository.save(currency);

        currencyRepository.delete(savedCurrency);

        Optional<Currency> findCurrency = currencyRepository.findById(savedCurrency.getId());

        assertThat(findCurrency).isEmpty();
    }
}
