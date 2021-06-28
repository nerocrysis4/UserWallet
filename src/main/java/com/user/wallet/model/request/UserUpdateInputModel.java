package com.user.wallet.model.request;

import com.user.wallet.model.entity.User;
import com.user.wallet.model.state.UserRole;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class UserUpdateInputModel {

  private Integer userId;
  private String name;
  private String email;
  private String location;

}
