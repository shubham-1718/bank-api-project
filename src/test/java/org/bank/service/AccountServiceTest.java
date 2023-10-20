package org.bank.service;

import org.bank.model.Account;
import org.bank.model.Customer;
import org.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(accountRepository);
    }

    @Test
    public void createAccountSucceeds() {
        Account account = new Account();
        account.setDeposit(BigDecimal.valueOf(100));

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Arisha Barron");
        account.setCustomer(customer);

        Mockito.when(accountRepository.save(account)).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);
        assertNotNull(createdAccount);
        assertEquals(BigDecimal.valueOf(100), createdAccount.getDeposit());

        verify(accountRepository).save(account);
    }

    @Test
    public void createAccountInvalidDeposit() {
        Account account = new Account();
        account.setDeposit(BigDecimal.valueOf(-100));

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(account));
    }

    @Test
    public void createAccountNullCustomer() {
        Account account = new Account();
        account.setDeposit(BigDecimal.valueOf(100));

        assertThrows(IllegalArgumentException.class, () -> accountService.createAccount(account));
    }

    @Test
    public void getAccountBalanceSucceeds() {
        Long accountId = 1L;
        Account account = new Account();
        account.setDeposit(BigDecimal.valueOf(200));
        Mockito.when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        BigDecimal balance = accountService.getAccountBalance(accountId);
        assertEquals(BigDecimal.valueOf(200), balance);
    }

    @Test
    public void getAccountBalanceNotFound() {
        Long accountId = 2L;
        Mockito.when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.getAccountBalance(accountId));
    }
}

