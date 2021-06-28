package com.user.wallet.service;

import com.user.wallet.model.entity.User;
import com.user.wallet.model.request.UserUpdateInputModel;
import com.user.wallet.model.response.framework.ResponseModel;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {

  ResponseModel signin(String username, String password);

  ResponseModel signup(User user);

  ResponseModel deleteUser(Integer userId);

  ResponseModel getUser(String username);

  ResponseModel updateUser(UserUpdateInputModel user);

}
