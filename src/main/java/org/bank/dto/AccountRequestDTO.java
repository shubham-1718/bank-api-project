package org.bank.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountRequestDTO {
    @NotNull
    private Long customerId;

    @NotNull
    private BigDecimal deposit;

    public AccountRequestDTO() {
    }

    public AccountRequestDTO(Long customerId, BigDecimal deposit) {
        this.customerId = customerId;
        this.deposit = deposit;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }
}
