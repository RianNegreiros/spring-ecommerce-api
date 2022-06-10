package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
import com.riannegreiros.springecommerce.modules.setting.repository.CurrencyRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.SettingRepository;
import com.riannegreiros.springecommerce.modules.setting.service.SettingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;
    private final CurrencyRepository currencyRepository;

    public SettingServiceImpl(SettingRepository settingRepository, CurrencyRepository currencyRepository) {
        this.settingRepository = settingRepository;
        this.currencyRepository = currencyRepository;
    }

    public List<Setting> findAllSettings() {
        return settingRepository.findAll();
    }

    @Override
    public List<Currency> findAllCurrencies() {
        return currencyRepository.findAll();
    }
}
