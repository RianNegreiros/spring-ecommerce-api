package com.riannegreiros.springecommerce.modules.setting.repository;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    List<State> findAllByOrderByNameAsc();
    List<State> findAllByCountryOrderByNameAsc(Country country);
    State findByName(String name);
}
