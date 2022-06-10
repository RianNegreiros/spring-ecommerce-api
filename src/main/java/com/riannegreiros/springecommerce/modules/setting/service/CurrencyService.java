package com.riannegreiros.springecommerce.modules.setting.service;

import com.riannegreiros.springecommerce.modules.setting.entity.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> findAll();
    Currency save(Currency currency);
    Currency update(Currency currency);
    void delete(Integer id);
}
