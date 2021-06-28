package com.user.wallet.model.entity;

import com.user.wallet.model.state.TransactionMode;
import com.user.wallet.model.state.UserRole;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

//  @Column(name = "sender_user_id")
//  private Integer senderUserId;
  @OneToOne
  @JoinColumn(name="sender_user_id")
  private User senderUser;

//  @Column(name = "receiver_user_id")
//  private Integer receiverUserId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name="receiver_user_id")
  private User receiverUser;

  @Column(name = "amount")
  private Integer amount;

  @Enumerated(EnumType.STRING)
  TransactionMode transactionMode;

  @Column(name = "transaction_time")
  private Date transactionTime;

}
