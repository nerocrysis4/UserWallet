package com.user.wallet.repository;

import com.user.wallet.model.entity.Transaction;
import com.user.wallet.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllBySenderUserIdOrReceiverUserId(Integer senderUserId, Integer receiverUserId);

}
