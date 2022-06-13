package com.riannegreiros.springecommerce.modules.customer;

import com.riannegreiros.springecommerce.exception.ResourceNotFoundException;
import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.customer.repository.CustomerRepository;
import com.riannegreiros.springecommerce.modules.customer.service.Impl.CustomerServiceImpl;
import com.riannegreiros.springecommerce.modules.customer.token.repository.TokenRepository;
import com.riannegreiros.springecommerce.modules.customer.token.service.Impl.TokenServiceImpl;
import com.riannegreiros.springecommerce.modules.setting.entity.Country;
import com.riannegreiros.springecommerce.modules.setting.entity.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    TokenRepository tokenRepository;

    @Mock
    TokenServiceImpl tokenService;

    CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        customerService = new CustomerServiceImpl(customerRepository, passwordEncoder, tokenService);

        Country country = new Country("any_name", "any_code");
        State state = new State("any_name", country);
        customer = new Customer();
        customer.setFirstName("any_name");
        customer.setLastName("any_name");
        customer.setEmail("any_mail@mail");
        customer.setPassword("any_password");
        customer.setPhoneNumber("any_phoneNumber");
        customer.setCity("any_city");
        customer.setState(state);
        customer.setCountry(country);
        customer.setPostalCode("any_postalCode");
        customer.setCreatedTime(new Date());
    }

    @Test
    public void testSaveCustomer() {
        customerService.save(customer);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer).isEqualTo(customer);
    }

    @Test
    public void testThrowIfEmailIsTaken() {
        given(customerRepository.findByEmail(any()))
                .willReturn(Optional.ofNullable(customer));

        assertThatThrownBy(() -> customerService.save(customer))
                .isInstanceOf(Error.class)
                .hasMessageContaining("Customer already exists with this email: " + customer.getEmail());

        verify(customerRepository, never()).save(any());
    }

    @Test
    public void testFindByEmail() {
        given(customerRepository.findByEmail(any()))
                .willReturn(Optional.ofNullable(customer));

        Customer customerExist = customerService.findByEmail(customer.getEmail());

        assertThat(customerExist).isNotNull();
        assertThat(customerExist).isEqualTo(customer);
    }

    @Test
    public void testThrowIfNotFoundByEmail() {
        given(customerRepository.findByEmail(any()))
                .willThrow(new ResourceNotFoundException("Customer", "Email", customer.getEmail()));

        assertThatThrownBy(() -> customerService.findByEmail(any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with %s : '%s'", "Customer", "Email", customer.getEmail()));
    }

    @Test
    public void testEnable() {
        given(customerRepository.findById(any()))
                .willReturn(Optional.ofNullable(customer));

        customerService.enable(customer.getId());

        assertThat(customer.getEnabled()).isTrue();
    }

    @Test
    public void testEnableThrowIfNotFindById() {
        given(customerRepository.findById(any()))
                .willThrow(new ResourceNotFoundException("Customer", "ID", null));

        assertThatThrownBy(() -> customerService.enable(any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with %s : '%s'", "Customer", "ID", null));
    }
}
