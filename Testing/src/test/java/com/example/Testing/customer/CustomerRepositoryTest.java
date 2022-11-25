package com.example.Testing.customer;

import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        // When
        // Then
    }

    // Failed this test
    @Test
    void itShouldSaveCustomerFail() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "0000");

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
//        assertThat(optionalCustomer).isNotPresent();
//        assertThat(optionalCustomer).isPresent();

        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo("Abel");
                    assertThat(c.getPhoneNumber()).isEqualTo("1111");
                });
    }


    // Failed this test
    @Test
    void itShouldSaveCustomer() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "0000");

        // When
        underTest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
//        assertThat(optionalCustomer).isNotPresent();
//        assertThat(optionalCustomer).isPresent();

        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getName()).isEqualTo("Abel");
                    assertThat(c.getPhoneNumber()).isEqualTo("0000");
                });
    }
}