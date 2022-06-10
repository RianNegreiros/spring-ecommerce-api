package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Country save(Country country) {
        Country countryExist = countryRepository.findByNameOrCode(country.getName(), country.getCode());
        if (countryExist != null) throw new Error("Country already exist");

        return countryRepository.save(country);
    }
}
