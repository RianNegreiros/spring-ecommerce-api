package com.riannegreiros.springecommerce.modules.setting.Country;

import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CountryRepositoryTests {

    @Autowired
    CountryRepository countryRepository;

    @Test
    public void testCreateCountry() {
        Country country = new Country("any_name", "any_code");
        Country savedCountry = countryRepository.save(country);

        assertThat(savedCountry).isNotNull();
        assertThat(savedCountry).isEqualTo(country);
    }

    @Test
    public void testFindAllByOrderByNameAsc() {
        Country country2 = new Country("any_name2", "any_code2");
        Country country1 = new Country("any_name1", "any_code1");
        Country country = new Country("any_name", "any_code");

        countryRepository.saveAll(List.of(country, country1, country2));

        List<Country> countries = countryRepository.findAllByOrderByNameAsc();

        assertThat(countries).isNotEmpty();
        assertThat(countries.get(0)).isEqualTo(country);
        assertThat(countries.get(1)).isEqualTo(country1);
        assertThat(countries.get(2)).isEqualTo(country2);
    }

    @Test
    public void testUpdate() {
        Country country = new Country("any_name", "any_code");
        Country savedCountry = countryRepository.save(country);

        savedCountry.setName("updated_name");
        Country updatedState = countryRepository.save(savedCountry);

        assertThat(updatedState.getName()).isEqualTo(savedCountry.getName());
    }
    @Test
    public void testDelete() {
        Country country = new Country("any_name", "any_code");
        Country savedCountry = countryRepository.save(country);

        countryRepository.delete(savedCountry);

        Optional<Country> findCountry = countryRepository.findById(savedCountry.getId());

        assertThat(findCountry).isEmpty();
    }
}
