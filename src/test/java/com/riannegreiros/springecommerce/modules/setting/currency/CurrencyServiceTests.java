package com.riannegreiros.springecommerce.modules.setting.currency;

import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.repository.CurrencyRepository;
import com.riannegreiros.springecommerce.modules.setting.service.Impl.CurrencyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTests {

    @Mock
    CurrencyRepository currencyRepository;
    CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyServiceImpl(currencyRepository);
    }

    @Test
    public void testSave() {
        Currency currency = new Currency("United States Dollar", "$", "USD");
        currencyService.save(currency);

        ArgumentCaptor<Currency> currencyArgumentCaptor = ArgumentCaptor.forClass(Currency.class);

        verify(currencyRepository).save(currencyArgumentCaptor.capture());

        Currency capturedCurrency = currencyArgumentCaptor.getValue();

        assertThat(capturedCurrency).isEqualTo(currency);
    }
}
