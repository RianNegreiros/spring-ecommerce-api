package com.riannegreiros.springecommerce.modules.setting.service.Impl;

import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
import com.riannegreiros.springecommerce.modules.setting.repository.SettingRepository;
import com.riannegreiros.springecommerce.modules.setting.service.SettingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    private SettingRepository settingRepository;

    public SettingServiceImpl(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public List<Setting> findAllSettings() {
        return settingRepository.findAll();
    }
}
