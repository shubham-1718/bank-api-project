package org.bank.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@SequenceGenerator(name = "transfer_id_sequence", sequenceName = "transfer_id_sequence", allocationSize = 1, initialValue = 1001)
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transfer_id_sequence")
    private Long transactionId;
    private Long accountId;
    private String narration;
    private LocalDateTime date;
    private BigDecimal withdrawalAmount;
    private BigDecimal depositAmount;
    private BigDecimal closingBalance;

    public Transfer() {
    }

    public Transfer(Long accountId, String narration, LocalDateTime date, BigDecimal withdrawalAmount, BigDecimal depositAmount, BigDecimal closingBalance) {
        this.accountId = accountId;
        this.narration = narration;
        this.date = date;
        this.withdrawalAmount = withdrawalAmount;
        this.depositAmount = depositAmount;
        this.closingBalance = closingBalance;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }
}
