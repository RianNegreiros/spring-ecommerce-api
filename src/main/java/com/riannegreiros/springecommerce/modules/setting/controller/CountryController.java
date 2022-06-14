package com.riannegreiros.springecommerce.modules.setting.controller;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.service.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Controller to handle the countries that the API accepts", tags = {"Country Controller"})
@RestController
@RequestMapping("/api/settings/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @ApiOperation(value = "Get all countries")
    @GetMapping()
    public ResponseEntity<List<Country>> findAll() {
        List<Country> countryList = countryService.findAll();
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

    @ApiOperation(value = "Add a new country")
    @PostMapping()
    public ResponseEntity<Country> save(@RequestBody Country country) {
        Country savedCountry = countryService.save(country);
        return new ResponseEntity<>(savedCountry, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Remove country")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id")Integer id) {
        countryService.delete(id);
        return new ResponseEntity<>("Country has been successfully deleted", HttpStatus.OK);
    }
}
