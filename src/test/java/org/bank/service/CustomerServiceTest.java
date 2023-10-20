package org.bank.service;

import org.bank.model.Customer;
import org.bank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void getCustomerByIdSucceeds() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(customerId);
        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
    }

    @Test
    public void getCustomerByIdNotFound() {
        Long customerId = 2L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Customer result = customerService.getCustomerById(customerId);
        assertNull(result);
    }

    @Test
    public void getCustomerByIdNullCustomerId() {
        Customer result = customerService.getCustomerById(null);
        assertNull(result);
    }
}

