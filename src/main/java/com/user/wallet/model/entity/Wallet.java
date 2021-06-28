package com.user.wallet.model.entity;

import com.user.wallet.model.state.UserRole;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

//  @Column(name = "user_id")
//  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name="user_id")
  private User user;

  @Column(name = "balance")
  private int balance;

  @Column(name = "last_updated_date")
  private Date lastUpdatedDate;

}
