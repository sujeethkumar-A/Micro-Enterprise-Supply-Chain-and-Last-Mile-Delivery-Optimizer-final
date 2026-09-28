package com.example.supplychain.repository;

import com.example.supplychain.model.ChartOfAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, Long> {
}
