package com.example.Testing.customer;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;

class CustomerRegistrationServiceTest_Mockito_V2 {

    private CustomerRepository customerRepository = mock(CustomerRepository.class);

    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerRegistrationService(customerRepository);
    }
}