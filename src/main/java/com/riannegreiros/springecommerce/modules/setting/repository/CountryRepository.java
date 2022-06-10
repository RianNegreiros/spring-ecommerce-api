package com.riannegreiros.springecommerce.modules.setting.repository;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country findByNameOrCode(String name, String code);
    List<Country> findAllByOrderByNameAsc();
}
