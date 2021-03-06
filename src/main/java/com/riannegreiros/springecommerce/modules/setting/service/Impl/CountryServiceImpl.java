package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Country> countryExist = countryRepository.findByNameOrCode(country.getName(), country.getCode());
        if (countryExist.isPresent()) throw new Error("Country already exist");

        return countryRepository.save(country);
    }

    @Override
    public void delete(Integer id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Country", "ID", id.toString()));
        countryRepository.delete(country);
    }
}
