package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.repository.CurrencyRepository;
import com.riannegreiros.springecommerce.modules.setting.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Currency save(Currency currency) {
        Optional<Currency> currencyExist = currencyRepository.findByNameOrCode(currency.getName(), currency.getCode());

        if (currencyExist.isPresent()) throw new Error("Currency already exist");

        return currencyRepository.save(currency);
    }

    @Override
    public Currency update(Currency currency) {
        Currency currencyExist = currencyRepository.findByNameOrCode(currency.getName(), currency.getCode()).orElseThrow(() -> new ResourceNotFoundException("Currency", "Name and Code", currency.getName() + " " + currency.getCode()));

        currencyExist.setName(currency.getName());
        currencyExist.setSymbol(currency.getSymbol());
        currencyExist.setCode(currency.getCode());
        return currencyRepository.save(currencyExist);
    }

    @Override
    public void delete(Integer id) {
        Currency currency = currencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency", "ID", id.toString()));

        currencyRepository.delete(currency);
    }
}
