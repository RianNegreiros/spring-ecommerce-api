package com.riannegreiros.springecommerce.modules.setting.Country;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.service.Impl.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTests {

    @Mock
    CountryRepository countryRepository;
    CountryServiceImpl countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryServiceImpl(countryRepository);
    }

    @Test
    public void testSave() {
        Country country = new Country("any_name", "any_code");
        countryService.save(country);

        ArgumentCaptor<Country> countryArgumentCaptor = ArgumentCaptor.forClass(Country.class);

        verify(countryRepository).save(countryArgumentCaptor.capture());

        Country capturedCountry = countryArgumentCaptor.getValue();

        assertThat(capturedCountry).isEqualTo(country);
    }

    @Test
    public void testDelete() {
        Country country = new Country("any_name", "any_code");

        given(countryRepository.findById(any()))
                .willReturn(Optional.of(country));

        countryService.delete(1);

        ArgumentCaptor<Country> countryArgumentCaptor = ArgumentCaptor.forClass(Country.class);

        verify(countryRepository, times(1)).delete(countryArgumentCaptor.capture());
    }
}