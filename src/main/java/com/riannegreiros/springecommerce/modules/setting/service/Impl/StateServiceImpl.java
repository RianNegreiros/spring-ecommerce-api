package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.repository.StateRepository;
import com.riannegreiros.springecommerce.modules.setting.service.StateService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public List<State> findAllStates() {
        return stateRepository.findAllByOrderByNameAsc();
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
