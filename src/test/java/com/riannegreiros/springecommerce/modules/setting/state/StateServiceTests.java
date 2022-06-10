package com.riannegreiros.springecommerce.modules.setting.state;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.StateRepository;
import com.riannegreiros.springecommerce.modules.setting.service.Impl.StateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StateServiceTests {

    @Mock
    private StateRepository stateRepository;
    @Mock
    private CountryRepository countryRepository;
    private StateServiceImpl stateService;

    @BeforeEach
    void setUp() {
        stateService = new StateServiceImpl(stateRepository, countryRepository);
    }

    @Test
    public void testFindAll() {
        Country country = new Country("any_name", "any_code");
        State state = new State("any_name", country);
        State state1 = new State("any_name1", country);
        given(stateRepository.findAllByOrderByNameAsc())
                .willReturn(List.of(state, state1));

        List<State> list = stateService.findAll();

        assertThat(list).isNotEmpty();
        assertThat(list.get(0)).isEqualTo(state);
        assertThat(list.get(1)).isEqualTo(state1);
    }

    @Test
    public void testFindAllByCountry() {
        Country country = new Country("any_name", "any_code");
        State state = new State("any_name", country);
        State state1 = new State("any_name1", country);

        given(countryRepository.findById(anyInt()))
                .willReturn(Optional.of(country));

        given(stateRepository.findAllByCountryOrderByNameAsc(any()))
                .willReturn(List.of(state, state1));

        stateService.findAllByCountry(anyInt());

        ArgumentCaptor<Country> countryArgumentCaptor = ArgumentCaptor.forClass(Country.class);

        verify(stateRepository).findAllByCountryOrderByNameAsc(countryArgumentCaptor.capture());
    }

    @Test
    public void testSave() {
        Country country = new Country("any_name", "any_code");
        State state = new State("any_name", country);
        stateService.save(state);

        ArgumentCaptor<State> stateArgumentCaptor = ArgumentCaptor.forClass(State.class);

        verify(stateRepository).save(stateArgumentCaptor.capture());

        State capturedState = stateArgumentCaptor.getValue();

        assertThat(capturedState).isEqualTo(state);
    }

    @Test
    public void testDelete() {
        Country country = new Country("any_name", "any_code");
        State state = new State("any_name", country);

        given(stateRepository.findById(any()))
                .willReturn(Optional.of(state));

        stateService.delete(1);

        ArgumentCaptor<State> stateArgumentCaptor = ArgumentCaptor.forClass(State.class);

        verify(stateRepository, times(1)).delete(stateArgumentCaptor.capture());
    }
}
