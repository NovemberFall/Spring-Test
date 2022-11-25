package com.example.Testing.customer;

import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;

    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... No customer with phone number passed
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        // Given
        String phoneNumber = "000099";
        UUID id = UUID.randomUUID();

        Customer customer = new Customer(id, "Maryam", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... an existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));


        // When
        underTest.registerNewCustomer(request);

        // Then
        then(customerRepository).should(never()).save(any());
//        then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
//        then(customerRepository).shouldHaveNoMoreInteractions();
    }


    @Test
    void itShouldThrowWhenPhoneNumberIsTaken() {
        // Given
        String phoneNumber = "000099";

        Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
        Customer customerTwo = new Customer(UUID.randomUUID(), "John", phoneNumber);

        // ... a request
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        // ... an existing customer is returned
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customerTwo));

        // When
        // Then
        assertThatThrownBy(()-> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is taken", phoneNumber));

        // Finally
        then(customerRepository).should(never()).save(any(Customer.class));
    }
}





























