package com.user.wallet.controller;

import com.user.wallet.model.request.UserInputModel;
import com.user.wallet.model.request.UserUpdateInputModel;
import com.user.wallet.model.response.framework.ApiResultModel;
import com.user.wallet.model.response.framework.ResponseModel;
import com.user.wallet.service.IUserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Api(tags = "users")
public class UserController {

  @Autowired
  private IUserService userService;

  @PostMapping("/signin")
  @ApiOperation(value = "User sign in, to get auth token")
  public ResponseEntity<ApiResultModel> userLogin(@RequestParam String username, @RequestParam String password) {
    log.info("API endpoint userLogin hit with user id : {}", username);
    ResponseModel response = userService.signin(username, password);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }

  @PostMapping("/signup")
  @ApiOperation(value = "User sign up, to create user and get auth token")
  public ResponseEntity<ApiResultModel> userSignup(@RequestBody UserInputModel inputUser) {
    log.info("API endpoint userSignup hit with username : {}", inputUser.getUsername());
    ResponseModel response = userService.signup(UserInputModel.mapUser(inputUser));
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{userId}")
  @ApiOperation(value = "Delete user account from wallet")
  public ResponseEntity<ApiResultModel> deleteUser(@PathVariable Integer userId) {
    log.info("API endpoint deleteUser hit with user id : {}", userId);
    ResponseModel response = userService.deleteUser(userId);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }

  @GetMapping(value = "/{username}")
  @ApiOperation(value = "Get the user details by username")
  public ResponseEntity<ApiResultModel> getUser(@PathVariable String username) {
    log.info("API endpoint getUser hit with username : {}", username);
    ResponseModel response = userService.getUser(username);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }

  @PutMapping("/{userId}")
  @ApiOperation(value = "User sign up, to create user and get auth token")
  public ResponseEntity<ApiResultModel> updateUser(@PathVariable Integer userId, @RequestBody UserUpdateInputModel inputUser) {
    log.info("API endpoint updateUser hit with user id : {} and body : {}", userId, inputUser);
    inputUser.setUserId(userId);
    ResponseModel response = userService.updateUser(inputUser);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }
}
