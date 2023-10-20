package org.bank.controller;

import org.bank.dto.AccountRequestDTO;
import org.bank.model.Account;
import org.bank.model.Customer;
import org.bank.service.AccountService;
import org.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    /**
     * Create new bank account for a customer.

     * @param accountRequest Account creation request.
     * @return ResponseEntity with the created account or NOT_FOUND status if the customer doesn't exist.
     */
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountRequestDTO accountRequest) {
        Long customerId = accountRequest.getCustomerId();
        Customer customer = customerService.getCustomerById(customerId);

        if (customer != null) {
            Account account = new Account(customer, accountRequest.getDeposit());
            account = accountService.createAccount(account);

            return new ResponseEntity<>(account, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieve the balance for a specific account.

     * @param accountId ID of the account.
     * @return ResponseEntity with the account balance or NOT_FOUND status if the account doesn't exist.
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable @NotNull Long accountId) {
        BigDecimal balance = accountService.getAccountBalance(accountId);
        if (balance != null) {
            return new ResponseEntity<>(balance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
