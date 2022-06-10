package com.riannegreiros.springecommerce.modules.setting.controller;

import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
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
    public ResponseEntity<List<Setting>> findAll() {
        List<Setting> settingList = settingService.findAllSettings();
        return new ResponseEntity<>(settingList, HttpStatus.OK);
    }
}
