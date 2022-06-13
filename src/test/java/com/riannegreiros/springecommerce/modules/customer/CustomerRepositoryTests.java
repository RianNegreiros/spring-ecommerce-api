package com.riannegreiros.springecommerce.modules.customer;

import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.customer.repository.CustomerRepository;
import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import com.riannegreiros.springecommerce.modules.setting.repository.CountryRepository;
import com.riannegreiros.springecommerce.modules.setting.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTests {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    StateRepository stateRepository;
    private Customer customer;
    private Customer savedCustomer;


    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setFirstName("any_name");
        customer.setLastName("any_name");
        customer.setEmail("any_mail@mail");
        customer.setPassword("any_password");
        customer.setPhoneNumber("any_phoneNumber");
        customer.setCity("any_city");
        Country country = new Country("any_name", "any_code");
        countryRepository.save(country);
        State state = new State("any_name", country);
        stateRepository.save(state);
        customer.setState(state);
        customer.setCountry(country);
        customer.setPostalCode("any_postalCode");
        customer.setCreatedTime(new Date());
        savedCustomer = customerRepository.save(customer);
    }

    @Test
    public void testSave() {
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer).isEqualTo(customer);
    }

    @Test
    public void testUpdate() {
        Customer findCustomer = customerRepository.findByEmail(savedCustomer.getEmail()).get();

        findCustomer.setEmail("updated@mail.com");
        findCustomer.setPhoneNumber("updated_phoneNumber");

        Customer updateCustomer = customerRepository.save(findCustomer);

        assertThat(savedCustomer.getEmail()).isEqualTo(updateCustomer.getEmail());
        assertThat(savedCustomer.getPhoneNumber()).isEqualTo(updateCustomer.getPhoneNumber());
    }

    @Test
    public void testDelete() {
        customerRepository.deleteById(savedCustomer.getId());

        boolean findCustomer = customerRepository.findById(savedCustomer.getId()).isPresent();

        assertThat(findCustomer).isFalse();
    }
}
