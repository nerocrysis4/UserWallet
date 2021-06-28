package com.user.wallet.model.request;

import com.user.wallet.model.entity.User;
import com.user.wallet.model.state.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class WalletInputModel {

  private User user;
  private int balance;

}
