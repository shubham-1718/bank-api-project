package org.bank.service;

import org.bank.dto.TransferRequestDTO;
import org.bank.exception.TransferException;
import org.bank.model.Account;
import org.bank.model.Transfer;
import org.bank.repository.AccountRepository;
import org.bank.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional(rollbackFor = TransferException.class)
    public void transfer(TransferRequestDTO transferRequest) {
        Long fromAccountId = transferRequest.getFromAccountId();
        Long toAccountId = transferRequest.getToAccountId();
        BigDecimal amount = transferRequest.getAmount();

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new TransferException("Source account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new TransferException("Destination account not found"));

        if (fromAccount.getDeposit().compareTo(amount) < 0) {
            throw new TransferException("Insufficient funds");
        }

        /* Deduct amount from the sender account */
        fromAccount.setDeposit(fromAccount.getDeposit().subtract(amount));

        /* Add amount to the recipient account */
        toAccount.setDeposit(toAccount.getDeposit().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        /* Create a transaction record for the sender */
        Transfer senderTransaction = new Transfer();
        senderTransaction.setAccountId(fromAccountId);
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            senderTransaction.setNarration("Withdrawn: " + amount + " to account: " + toAccountId);
            senderTransaction.setWithdrawalAmount(amount);
            senderTransaction.setDepositAmount(BigDecimal.ZERO);
        }
        senderTransaction.setDate(LocalDateTime.now());
        senderTransaction.setClosingBalance(fromAccount.getDeposit());

        /* Create a transaction record for the recipient */
        Transfer recipientTransaction = new Transfer();
        recipientTransaction.setAccountId(toAccountId);
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            recipientTransaction.setNarration("Deposited: " + amount + " from account: " + fromAccountId);
            recipientTransaction.setWithdrawalAmount(BigDecimal.ZERO);
            recipientTransaction.setDepositAmount(amount);
        }
        recipientTransaction.setDate(LocalDateTime.now());
        recipientTransaction.setClosingBalance(toAccount.getDeposit());

        transferRepository.save(senderTransaction);
        transferRepository.save(recipientTransaction);
    }

    public List<Transfer> getTransferHistoryByAccount(Long accountId) {
        return transferRepository.findTransfersByAccountId(accountId);
    }
}
