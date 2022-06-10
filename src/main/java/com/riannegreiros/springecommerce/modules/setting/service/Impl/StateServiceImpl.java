package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.StateRepository;
import com.riannegreiros.springecommerce.modules.setting.service.StateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    private final CountryRepository countryRepository;

    public StateServiceImpl(StateRepository stateRepository, CountryRepository countryRepository) {
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<State> findAll() {
        return stateRepository.findAllByOrderByNameAsc();
    }

    @Override
    public List<State> findAllByCountry(Integer id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country", "ID", id.toString()));
        return stateRepository.findAllByCountryOrderByNameAsc(country);
    }

    @Override
    public State save(State state) {
        State stateExists = stateRepository.findByName(state.getName());

        if (stateExists != null) throw new Error("State already exist by the name: " + stateExists.getName());

        return stateRepository.save(state);
    }

    @Override
    public void delete(Integer id) {
        State stateExist = stateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("State", "ID", id.toString()));

        stateRepository.delete(stateExist);
    }
}
