package com.user.wallet.service;

import com.user.wallet.model.request.WalletAddMoneyInputModel;
import com.user.wallet.model.request.WalletInputModel;
import com.user.wallet.model.request.WalletSendMoneyInputModel;
import com.user.wallet.model.response.framework.ResponseModel;
import org.springframework.stereotype.Service;

@Service
public interface IWalletService {

  ResponseModel createWallet(WalletInputModel input);

  ResponseModel getWallet(Integer userId);

  ResponseModel addMoney(WalletAddMoneyInputModel inputModel);

  ResponseModel sendMoney(WalletSendMoneyInputModel inputModel);

  ResponseModel getPassbook(Integer userId);

}
