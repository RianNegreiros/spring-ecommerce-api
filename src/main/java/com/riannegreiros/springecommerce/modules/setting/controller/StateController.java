package com.riannegreiros.springecommerce.modules.setting.controller;

import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.service.StateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings/states")
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping
    public ResponseEntity<List<State>> findAll() {
        List<State> countryList = stateService.findAllStates();
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<State> save(@RequestBody State state) {
        State savedState = stateService.save(state);
        return new ResponseEntity<>(savedState, HttpStatus.CREATED);
    }
}
