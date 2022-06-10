package com.riannegreiros.springecommerce.modules.setting.service;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
import com.riannegreiros.springecommerce.modules.setting.entity.State;

import java.util.List;

public interface SettingService {
    List<Setting> findAllSettings();

    List<Currency> findAllCurrencies();
    List<Country> findAllCountries();
    List<State> findAllStates();
}
