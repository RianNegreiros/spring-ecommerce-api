package com.riannegreiros.springecommerce.modules.setting.controller;

import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.service.StateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Controller to handle the states that the API accepts", tags = {"State Controller"})
@RestController
@RequestMapping("/api/settings/states")
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @ApiOperation(value = "Get all states")
    @GetMapping
    public ResponseEntity<List<State>> findAll() {
        List<State> stateList = stateService.findAll();
        return new ResponseEntity<>(stateList, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all states by country")
    @GetMapping("/{id}")
    public ResponseEntity<List<State>> findByCountry(@PathVariable(name = "id") Integer id) {
        List<State> stateList = stateService.findAllByCountry(id);
        return new ResponseEntity<>(stateList, HttpStatus.OK);
    }

    @ApiOperation(value = "Add new state")
    @PostMapping
    public ResponseEntity<State> save(@RequestBody State state) {
        State savedState = stateService.save(state);
        return new ResponseEntity<>(savedState, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Remove a state")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        stateService.delete(id);
        return new ResponseEntity<>("State has been deleted successfully", HttpStatus.OK);
    }
}
