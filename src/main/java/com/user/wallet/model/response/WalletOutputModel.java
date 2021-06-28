package com.user.wallet.model.response;

import com.user.wallet.model.entity.User;
import com.user.wallet.model.entity.Wallet;
import com.user.wallet.model.state.UserRole;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
public class WalletOutputModel {

  private Integer id;
  private String username;
  private String name;
  private int balance;
  private Date lastUpdatedDate;

  public static WalletOutputModel mapUser(Wallet wallet){
    if(wallet == null)
      return null;
    WalletOutputModel responseDTO = new WalletOutputModel();
    responseDTO.setId(wallet.getId());
    responseDTO.setUsername(wallet.getUser().getUsername());
    responseDTO.setName(wallet.getUser().getName());
    responseDTO.setBalance(wallet.getBalance());
    responseDTO.setLastUpdatedDate(wallet.getLastUpdatedDate());

    return responseDTO;
  }
}
