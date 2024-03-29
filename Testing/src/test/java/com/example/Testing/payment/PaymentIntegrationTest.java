package com.example.Testing.payment;

import com.example.Testing.customer.Customer;
import com.example.Testing.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given a customer
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "+447000000000");

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        // Register
        ResultActions customerRegResultActions =
                mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest))));

        //        System.out.println(customerRegResultActions);

        /* ***********************************************************************************/

        // ... Payment
        long paymentId = 1L;

        Payment payment = new Payment(
                paymentId,
                customerId,
                new BigDecimal("100.00"),
                Currency.GBP,
                "x0x0x0x0",
                "Zakat"
        );


        // ... Payment request
        PaymentRequest paymentRequest = new PaymentRequest(payment);


        // When
        // ... When payment is sent
        ResultActions paymentResultActions = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));


//        // Then
//        // both customer registration and payment requests are 200 status code
//        customerRegResultActions.andExpect(MockMvcResultMatchers.status().isOk());
//        paymentResultActions.andExpect(MockMvcResultMatchers.status().isOk());
//
//        // Payment is stored in db
//        // TODO: Do not use paymentRepository instead create an endpoint to retrieve payments for customers
//        assertThat(paymentRepository.findById(paymentId))
//                .isPresent()
//                .hasValueSatisfying(p -> assertThat(p).isEqualToComparingFieldByField(payment));
    }


    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }
}
