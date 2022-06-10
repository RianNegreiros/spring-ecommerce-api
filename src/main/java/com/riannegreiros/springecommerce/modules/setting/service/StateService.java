package com.riannegreiros.springecommerce.modules.setting.service;

import com.riannegreiros.springecommerce.modules.setting.entity.State;

import java.util.List;

public interface StateService {
    List<State> findAllStates();
    State save(State state);
    void delete(Integer id);
}
