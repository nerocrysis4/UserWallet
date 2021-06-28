package com.user.wallet.model.response;

import com.user.wallet.model.state.TransactionMode;
import com.user.wallet.model.state.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class PassbookDetails {

  private Integer amount;
  TransactionMode mode;
  TransactionType type;
  String targetUsername;
  private Date transactionTime;

}
