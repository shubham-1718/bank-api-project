package org.bank.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@SequenceGenerator(name = "account_id_sequence", sequenceName = "account_id_sequence", allocationSize = 1, initialValue = 101)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_sequence")
    private Long accountId;

    private BigDecimal deposit;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    public Account() {
    }

    public Account(Customer customer, BigDecimal deposit) {
        this.customer = customer;
        this.deposit = deposit;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
