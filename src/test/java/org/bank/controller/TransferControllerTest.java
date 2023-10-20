package org.bank.controller;

import org.bank.dto.TransferRequestDTO;
import org.bank.model.Transfer;
import org.bank.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferControllerTest {

    @Mock
    private TransferService transferService;

    private TransferController transferController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        transferController = new TransferController(transferService);
    }

    @Test
    public void transferAmountSucceeds() {
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO(1L, 2L, BigDecimal.valueOf(100));
        Mockito.doNothing().when(transferService).transfer(transferRequestDTO);
        ResponseEntity<String> response = transferController.transferAmount(transferRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transfer successful", response.getBody());
    }

    @Test
    public void transferAmountFails() {
        TransferRequestDTO transferRequestDTO = new TransferRequestDTO(1L, 2L, BigDecimal.valueOf(100));
        Mockito.doThrow(new RuntimeException("Transfer failed")).when(transferService).transfer(transferRequestDTO);
        ResponseEntity<String> response = transferController.transferAmount(transferRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Transfer failed", response.getBody());
    }

    @Test
    public void getTransferHistorySucceeds() {
        Long accountId = 1L;
        List<Transfer> transferHistory = new ArrayList<>();
        Mockito.when(transferService.getTransferHistoryByAccount(accountId)).thenReturn(transferHistory);
        ResponseEntity<List<Transfer>> response = transferController.getTransferHistory(accountId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transferHistory, response.getBody());
    }

    @Test
    public void getTransferHistoryNotFound() {
        Long accountId = 1L;
        Mockito.when(transferService.getTransferHistoryByAccount(accountId)).thenReturn(null);
        ResponseEntity<List<Transfer>> response = transferController.getTransferHistory(accountId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

