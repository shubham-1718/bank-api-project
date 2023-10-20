package org.bank.controller;

import org.bank.dto.TransferRequestDTO;
import org.bank.model.Transfer;
import org.bank.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    private TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Transfer funds between accounts.

     * @param transferRequestDTO Transfer request.
     * @return ResponseEntity with a success message or BAD_REQUEST status with an error message.
     */
    @PostMapping
    public ResponseEntity<String> transferAmount(@Valid @RequestBody TransferRequestDTO transferRequestDTO) {
        try {
            transferService.transfer(transferRequestDTO);
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Get transfer history for a specific account.

     * @param accountId ID of the account.
     * @return ResponseEntity with the transfer history or NOT_FOUND status if the account doesn't exist.
     */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<Transfer>> getTransferHistory(@PathVariable Long accountId) {
        List<Transfer> transferHistory = transferService.getTransferHistoryByAccount(accountId);
        if (transferHistory != null) {
            return new ResponseEntity<>(transferHistory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
