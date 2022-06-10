package com.riannegreiros.springecommerce.modules.setting.repository;

import com.riannegreiros.springecommerce.modules.setting.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    Currency findByNameOrCode(String name, String code);
    List<Currency> findAllByOrderByNameAsc();
}
