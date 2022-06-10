package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.CurrencyRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.SettingRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.StateRepository;
import com.riannegreiros.springecommerce.modules.setting.service.SettingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;

    public SettingServiceImpl(SettingRepository settingRepository, CurrencyRepository currencyRepository, CountryRepository countryRepository, StateRepository stateRepository) {
        this.settingRepository = settingRepository;
        this.currencyRepository = currencyRepository;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
    }

    @Override
    public List<Setting> findAllSettings() {
        return settingRepository.findAll();
    }

    @Override
    public List<Currency> findAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public List<Country> findAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    @Override
    public List<State> findAllStates() {
        return stateRepository.findAllByOrderByNameAsc();
    }
}
