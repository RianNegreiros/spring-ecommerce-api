package com.riannegreiros.springecommerce.modules.setting.repository;

import com.riannegreiros.springecommerce.modules.setting.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
}
