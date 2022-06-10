package com.riannegreiros.springecommerce.modules.setting.state;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StateRepositoryTests {

    @Autowired
    StateRepository stateRepository;
    @Autowired
    CountryRepository countryRepository;
    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country("any_name", "any_code");
        countryRepository.save(country);
    }

    @Test
    public void testCreateState() {
        State state = new State("any_name", country);
        State savedState = stateRepository.save(state);

        assertThat(savedState).isNotNull();
        assertThat(savedState).isEqualTo(state);
    }

    @Test
    public void testFindAllByOrderByNameAsc() {
        State state2 = new State("any_name2", country);
        State state1 = new State("any_name1", country);
        State state = new State("any_name", country);

        stateRepository.saveAll(List.of(state, state1, state2));

        List<State> states = stateRepository.findAllByOrderByNameAsc();

        assertThat(states).isNotEmpty();
        assertThat(states.get(0)).isEqualTo(state);
        assertThat(states.get(1)).isEqualTo(state1);
        assertThat(states.get(2)).isEqualTo(state2);
    }

    @Test
    public void testUpdate() {
        State state = new State("any_name", country);
        State savedState = stateRepository.save(state);

        savedState.setName("updated_name");
        State updatedState = stateRepository.save(savedState);

        assertThat(updatedState.getName()).isEqualTo(savedState.getName());
    }
    @Test
    public void testDelete() {
        State state = new State("any_name", country);
        State savedState = stateRepository.save(state);

        stateRepository.delete(savedState);

        Optional<State> findState = stateRepository.findById(savedState.getId());

        assertThat(findState).isEmpty();
    }
}
