package com.user.wallet.model.request;

import com.user.wallet.model.entity.User;
import com.user.wallet.model.state.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserInputModel {
  
  private String username;
  private String name;
  private String email;
  private String password;
  List<UserRole> userRoles;

  public static User mapUser(UserInputModel dataDTO){
    if(dataDTO == null)
      return null;
    User user = new User();
    user.setPassword(dataDTO.getPassword());
    user.setName(dataDTO.getName());
    user.setUserRoles(dataDTO.getUserRoles());
    user.setUsername(dataDTO.getUsername());
    user.setEmail(dataDTO.getEmail());

    return user;
  }

}
