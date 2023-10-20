package org.bank.service;

import org.bank.dto.TransferRequestDTO;
import org.bank.exception.TransferException;
import org.bank.model.Account;
import org.bank.model.Transfer;
import org.bank.repository.AccountRepository;
import org.bank.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    private TransferService transferService;

    @BeforeEach
    public void setUp() {
        transferService = new TransferService(accountRepository, transferRepository);
    }

    @Test
    public void transferSuccess() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100.0);

        Account fromAccount = new Account();
        fromAccount.setAccountId(fromAccountId);
        fromAccount.setDeposit(amount);

        Account toAccount = new Account();
        toAccount.setAccountId(toAccountId);
        toAccount.setDeposit(BigDecimal.ZERO);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        transferService.transfer(new TransferRequestDTO(fromAccountId, toAccountId, amount));

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);

        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);
        verify(transferRepository, times(2)).save(transferCaptor.capture());

        List<Transfer> savedTransfers = transferCaptor.getAllValues();

        assertEquals(2, savedTransfers.size());

        Transfer senderTransaction = savedTransfers.get(0);
        Transfer recipientTransaction = savedTransfers.get(1);

        assertEquals("Withdrawn: 100.0 to account: 2", senderTransaction.getNarration());
        assertEquals(BigDecimal.valueOf(100.0), senderTransaction.getWithdrawalAmount());
        assertEquals(BigDecimal.ZERO, senderTransaction.getDepositAmount());
        assertEquals(BigDecimal.valueOf(0.0), senderTransaction.getClosingBalance());

        assertEquals("Deposited: 100.0 from account: 1", recipientTransaction.getNarration());
        assertEquals(BigDecimal.ZERO, recipientTransaction.getWithdrawalAmount());
        assertEquals(BigDecimal.valueOf(100.0), recipientTransaction.getDepositAmount());
        assertEquals(BigDecimal.valueOf(100.0), recipientTransaction.getClosingBalance());
    }


    @Test
    public void transferInsufficientFunds() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100.0);

        Account fromAccount = new Account();
        fromAccount.setAccountId(fromAccountId);
        fromAccount.setDeposit(BigDecimal.ZERO);

        Account toAccount = new Account();
        toAccount.setAccountId(toAccountId);
        toAccount.setDeposit(BigDecimal.ZERO);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        TransferException exception = assertThrows(TransferException.class, () -> transferService.transfer(new TransferRequestDTO(fromAccountId, toAccountId, amount)));

        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    public void transferAccountNotFound() {
        long fromAccountId = 1L;
        long toAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100.0);

        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.empty());
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.empty());

        TransferException exception = assertThrows(TransferException.class, () -> transferService.transfer(new TransferRequestDTO(fromAccountId, toAccountId, amount)));

        assertEquals("Source account not found", exception.getMessage());
    }
}

