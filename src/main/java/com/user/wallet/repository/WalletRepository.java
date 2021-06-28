package com.user.wallet.repository;

import com.user.wallet.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

  Wallet findByUserId(Integer username);

}
