package com.riannegreiros.springecommerce.modules.setting.state;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.repository.StateRepository;
import com.riannegreiros.springecommerce.modules.setting.service.Impl.StateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StateRepositoryTests {

    @Mock
    StateRepository stateRepository;

    StateServiceImpl stateService;

    @BeforeEach
    void setUp() {
        stateService = new StateServiceImpl(stateRepository);
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
}
