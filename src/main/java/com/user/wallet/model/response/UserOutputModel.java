package com.user.wallet.model.response;

import com.user.wallet.model.entity.User;
import com.user.wallet.model.state.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserOutputModel {

  private Integer id;
  private String username;
  private String name;
  private String email;
  List<UserRole> userRoles;

  public static UserOutputModel mapUser(User user){
    if(user == null)
      return null;
    UserOutputModel responseDTO = new UserOutputModel();
    responseDTO.setId(user.getId());
    responseDTO.setUsername(user.getUsername());
    responseDTO.setUserRoles(user.getUserRoles());
    responseDTO.setEmail(user.getEmail());
    responseDTO.setName(user.getName());

    return responseDTO;
  }
}
