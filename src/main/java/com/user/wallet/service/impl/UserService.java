package com.user.wallet.service.impl;

import com.user.wallet.exception.WalletException;
import com.user.wallet.model.entity.User;
import com.user.wallet.model.request.UserUpdateInputModel;
import com.user.wallet.model.request.WalletInputModel;
import com.user.wallet.model.response.framework.ResponseModel;
import com.user.wallet.model.response.UserOutputModel;
import com.user.wallet.repository.UserRepository;
import com.user.wallet.security.JwtTokenProvider;
import com.user.wallet.service.IUserService;
import com.user.wallet.service.IWalletService;
import com.user.wallet.utils.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService implements IUserService {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private IWalletService walletService;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Override
  public ResponseModel signin(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      String token = "Bearer " + jwtTokenProvider.createToken(username, userRepo.findByUsername(username).getUserRoles());
      return new ResponseModel(token, "Auth token retrieve successfully");
    } catch (AuthenticationException e) {
      log.error("Invalid username or password for username : {}", username);
      throw new WalletException("Invalid username or password", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @Transactional
  @Override
  public ResponseModel signup(User user) {
    if (!userRepo.existsByUsername(user.getUsername())) {
      user.setPassword(encoder.encode(user.getPassword()));
      user.setCreatedDate(DateUtility.getTime());
      user.setLastUpdatedDate(DateUtility.getTime());
      user.setPassword(encoder.encode(user.getPassword()));
      userRepo.save(user);
      walletService.createWallet(WalletInputModel.builder().user(user).balance(0).build());
      String token = "Bearer " + jwtTokenProvider.createToken(user.getUsername(), user.getUserRoles());
      return new ResponseModel(token, "Auth token retrieve successfully");
    } else {
      log.error("Username already used for username : {}", user.getUsername());
      throw new WalletException("Username already used", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  @Transactional
  @Override
  public ResponseModel deleteUser(Integer userId) {
    User existingUser = userRepo.findById(userId).orElse(null);
    if (existingUser == null) {
      log.error("user does not exist for user id : {}", userId);
      throw new WalletException("user does not exist", HttpStatus.NOT_FOUND);
    }
    userRepo.delete(existingUser);
    return new ResponseModel("User delete successfully");
  }

  @Override
  public ResponseModel getUser(String username) {
    User user = userRepo.findByUsername(username);
    if (user == null) {
      log.error("user does not exist for username : {}", username);
      throw new WalletException("The user doesn't exist", HttpStatus.NOT_FOUND);
    }
    return new ResponseModel(UserOutputModel.mapUser(user), "User profile get successfully");
  }

  @Override
  public ResponseModel updateUser(UserUpdateInputModel inputModel) {
    if (inputModel == null) {
      log.error("Please provide valid input");
      throw new WalletException("Please provide valid input", HttpStatus.BAD_REQUEST);
    }
    User existingUser = userRepo.findById(inputModel.getUserId()).orElse(null);
    if (existingUser == null) {
      log.error("user does not exist for username : {}", inputModel.getUserId());
      throw new WalletException("user does not exist", HttpStatus.NOT_FOUND);
    }
    existingUser.setEmail(inputModel.getEmail());
    existingUser.setName(inputModel.getName());
    existingUser.setLastUpdatedDate(DateUtility.getTime());
    userRepo.save(existingUser);
    return new ResponseModel("User profile update successfully");

  }

}
