package com.riannegreiros.springecommerce.modules.setting.controller;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.service.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping()
    public ResponseEntity<List<Setting>> findAllSettings() {
        List<Setting> settingList = settingService.findAllSettings();
        return new ResponseEntity<>(settingList, HttpStatus.OK);
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<Currency>> findAllCurrencies() {
        List<Currency> currencyList = settingService.findAllCurrencies();
        return new ResponseEntity<>(currencyList, HttpStatus.OK);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> findAllCountries() {
        List<Country> countryList = settingService.findAllCountries();
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }
    @GetMapping("/states")
    public ResponseEntity<List<State>> findAllStates() {
        List<State> countryList = settingService.findAllStates();
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

}
