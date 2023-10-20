package org.bank.controller;

import org.bank.dto.AccountRequestDTO;
import org.bank.model.Account;
import org.bank.model.Customer;
import org.bank.service.AccountService;
import org.bank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        accountController = new AccountController(accountService, customerService);
    }

    @Test
    void createAccountWhenCustomerExists() {
        AccountRequestDTO request = new AccountRequestDTO(1L, new BigDecimal("1000.00"));
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Test Customer");

        when(customerService.getCustomerById(1L)).thenReturn(customer);

        Account account = new Account(customer, request.getDeposit());
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        ResponseEntity<Account> response = accountController.createAccount(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void createAccountCustomerNotFound() {
        AccountRequestDTO request = new AccountRequestDTO(2L, new BigDecimal("1000.00"));

        when(customerService.getCustomerById(2L)).thenReturn(null);

        ResponseEntity<Account> response = accountController.createAccount(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAccountBalanceWhenAccountExists() {
        Account account = new Account();
        account.setAccountId(1L);
        account.setDeposit(new BigDecimal("2000.00"));

        when(accountService.getAccountBalance(1L)).thenReturn(account.getDeposit());

        ResponseEntity<BigDecimal> response = accountController.getAccountBalance(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account.getDeposit(), response.getBody());
    }

    @Test
    void getAccountBalanceNotFound() {
        when(accountService.getAccountBalance(3L)).thenReturn(null);

        ResponseEntity<BigDecimal> response = accountController.getAccountBalance(3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

