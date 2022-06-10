package com.riannegreiros.springecommerce.modules.setting.service;

import com.riannegreiros.springecommerce.modules.setting.entity.Setting;

import java.util.List;

public interface SettingService {
    List<Setting> findAllSettings();
}
