package com.riannegreiros.springecommerce.modules.setting;

import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
import com.riannegreiros.springecommerce.modules.setting.entity.SettingCategory;
import com.riannegreiros.springecommerce.modules.setting.repository.SettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SettingRepositoryTest {

    @Autowired
    SettingRepository settingRepository;

    @Test
    public void testSaveSetting() {
        Setting setting = new Setting("any_key", "any_value", SettingCategory.GENERAL);

        Setting savedSetting = settingRepository.save(setting);

        assertThat(savedSetting).isNotNull();
    }
}
