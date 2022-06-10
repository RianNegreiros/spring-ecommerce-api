package com.riannegreiros.springecommerce.modules.setting.service;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;

import java.util.List;

public interface CountryService {
    List<Country> findAll();
    Country save(Country country);
}
