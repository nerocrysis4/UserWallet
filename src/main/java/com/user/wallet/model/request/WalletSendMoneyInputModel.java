package com.user.wallet.model.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
public class WalletSendMoneyInputModel {

  private int senderUserId;

  private int receiverUserId;

  @Min(value = 1, message="must be a positive number")
  private int amount;

}
