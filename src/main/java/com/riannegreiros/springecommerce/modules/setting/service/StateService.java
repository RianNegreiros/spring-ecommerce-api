package com.riannegreiros.springecommerce.modules.setting.service;

import com.riannegreiros.springecommerce.modules.setting.entity.State;

import java.util.List;

public interface StateService {
    List<State> findAll();
    List<State> findAllByCountry(Integer id);
    State save(State state);
    void delete(Integer id);
}
