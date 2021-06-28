package com.user.wallet.controller;

import com.user.wallet.model.request.WalletAddMoneyInputModel;
import com.user.wallet.model.request.WalletSendMoneyInputModel;
import com.user.wallet.model.response.framework.ApiResultModel;
import com.user.wallet.model.response.framework.ResponseModel;
import com.user.wallet.service.IWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/wallet")
@Api(tags = "wallet")
public class WalletController {

  @Autowired
  private IWalletService walletService;

  @GetMapping(value = "/{userId}")
  @ApiOperation(value = "Get wallet details by user id")
  public ResponseEntity<ApiResultModel> getWallet(@PathVariable Integer userId) {
    log.info("API endpoint getWallet hit with user id : {}", userId);
    ResponseModel response = walletService.getWallet(userId);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }

  @PutMapping("/{userId}/addmoney")
  @ApiOperation(value = "Add money to the wallet")
  public ResponseEntity<ApiResultModel> addMoney(@PathVariable Integer userId, @Valid @RequestBody WalletAddMoneyInputModel inputModel) {
    log.info("API endpoint addMoney hit with user id : {}, and body : {}", userId, inputModel);
    inputModel.setUserId(userId);
    ResponseModel response = walletService.addMoney(inputModel);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }

  @PutMapping("/sendmoney")
  @ApiOperation(value = "Send money")
  public ResponseEntity<ApiResultModel> sendMoney(@Valid @RequestBody WalletSendMoneyInputModel inputModel) {
    log.info("API endpoint sendMoney hit with body : {}", inputModel);
    ResponseModel response = walletService.sendMoney(inputModel);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }

  @GetMapping("/{userId}/passbook")
  @ApiOperation(value = "Get the passbook")
  public ResponseEntity<ApiResultModel> getPassbook(@PathVariable Integer userId) {
    log.info("API endpoint getPassbook hit with user id : {}", userId);
    ResponseModel response = walletService.getPassbook(userId);
    return new ResponseEntity<ApiResultModel>(new ApiResultModel(false, response.getMsg(), response), HttpStatus.OK);
  }
}
