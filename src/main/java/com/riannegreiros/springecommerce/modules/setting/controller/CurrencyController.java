package com.riannegreiros.springecommerce.modules.setting.controller;

import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import com.riannegreiros.springecommerce.modules.setting.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> findAll() {
        List<Currency> currencyList = currencyService.findAll();
        return new ResponseEntity<>(currencyList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Currency> save(@RequestBody Currency currency) {
        Currency savedCurrency = currencyService.save(currency);
        return new ResponseEntity<>(savedCurrency, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Currency> update(@RequestBody Currency currency) {
        Currency updatedCurrency = currencyService.update(currency);
        return new ResponseEntity<>(updatedCurrency, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        currencyService.delete(id);
        return new ResponseEntity<>("Currency has been successfully deleted", HttpStatus.OK);
    }
}
