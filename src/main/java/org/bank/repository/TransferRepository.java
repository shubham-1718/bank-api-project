package org.bank.repository;

import org.bank.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findTransfersByAccountId(Long accountId);
}
