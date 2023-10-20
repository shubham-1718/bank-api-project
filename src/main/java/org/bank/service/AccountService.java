package org.bank.service;

import org.bank.model.Account;
import org.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates new bank account with proper validation.

     * @param account The account to be created.
     * @return The created account.
     * @throws IllegalArgumentException if the account is not valid.
     */
    @Transactional
    public Account createAccount(@NotNull Account account) {
        validateAccount(account);

        return accountRepository.save(account);
    }

    /**
     * Retrieves the balance of a bank account.

     * @param accountId ID of account to retrieve the balance for.
     * @return The balance of a specified account.
     * @throws EntityNotFoundException If the account is not found.
     */
    public BigDecimal getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        return account.getDeposit();
    }

    private void validateAccount(Account account) {
        if (account.getDeposit() == null || account.getDeposit().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Deposit amount must be a non-negative value.");
        }
        if (account.getCustomer() == null) {
            throw new IllegalArgumentException("Customer information is required.");
        }
    }
}
