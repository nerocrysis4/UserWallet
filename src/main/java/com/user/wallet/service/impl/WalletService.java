package com.user.wallet.service.impl;

import com.user.wallet.exception.WalletException;
import com.user.wallet.model.entity.Transaction;
import com.user.wallet.model.entity.User;
import com.user.wallet.model.entity.Wallet;
import com.user.wallet.model.request.WalletAddMoneyInputModel;
import com.user.wallet.model.request.WalletInputModel;
import com.user.wallet.model.request.WalletSendMoneyInputModel;
import com.user.wallet.model.response.PassbookDetails;
import com.user.wallet.model.response.PassbookResponseModel;
import com.user.wallet.model.response.WalletOutputModel;
import com.user.wallet.model.response.framework.ResponseModel;
import com.user.wallet.model.state.TransactionMode;
import com.user.wallet.model.state.TransactionType;
import com.user.wallet.repository.TransactionRepository;
import com.user.wallet.repository.WalletRepository;
import com.user.wallet.service.IWalletService;
import com.user.wallet.utils.DateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WalletService implements IWalletService {

  @Autowired
  private WalletRepository walletRepo;

  @Autowired
  private TransactionRepository transactionRepo;

  public ResponseModel createWallet(WalletInputModel input) {
    Wallet wallet = new Wallet();
    wallet.setBalance(input.getBalance());
    wallet.setUser(input.getUser());
    wallet.setLastUpdatedDate(DateUtility.getTime());
    walletRepo.save(wallet);
    return new ResponseModel(null, "Wallet create successfully");

  }

  @Override
  public ResponseModel getWallet(Integer userId) {
    Wallet wallet = walletRepo.findByUserId(userId);
    if (wallet == null) {
      log.error("Wallet details not found for user id : {}", userId);
      throw new WalletException("Wallet details not found", HttpStatus.NOT_FOUND);
    }
    return new ResponseModel(WalletOutputModel.mapUser(wallet), "Wallet details fetch successfully");
  }

  @Transactional
  @Override
  public ResponseModel addMoney(WalletAddMoneyInputModel inputModel) {
    if (inputModel == null) {
      log.error("Please provide valid input");
      throw new WalletException("Please provide valid input", HttpStatus.BAD_REQUEST);
    }
    if (inputModel.getAmount() <= 0) {
      log.error("Entered amount must be positive for user id : {}", inputModel.getUserId());
      throw new WalletException("Please enter positive amount", HttpStatus.BAD_REQUEST);
    }
    Wallet existingWallet = walletRepo.findByUserId(inputModel.getUserId());
    if (existingWallet == null) {
      log.error("Wallet details not found for user id : {}", inputModel.getUserId());
      throw new WalletException("Wallet details not found", HttpStatus.NOT_FOUND);
    }
    final Integer updatedBalance = existingWallet.getBalance() + inputModel.getAmount();
    existingWallet.setBalance(updatedBalance);
    existingWallet.setLastUpdatedDate(DateUtility.getTime());

    walletRepo.save(existingWallet);
    transactionRepo.save(getTransaction(inputModel, existingWallet));

    return new ResponseModel("Money added to wallet successfully");
  }

  private Transaction getTransaction(WalletAddMoneyInputModel inputModel, Wallet existingWallet) {
    Transaction transaction = new Transaction();
    transaction.setTransactionMode(TransactionMode.UPI);
    transaction.setTransactionTime(DateUtility.getTime());
    transaction.setReceiverUser(existingWallet.getUser());
    transaction.setAmount(inputModel.getAmount());
    return transaction;
  }

  @Transactional
  @Override
  public ResponseModel sendMoney(WalletSendMoneyInputModel inputModel) {
    if (inputModel == null) {
      log.error("Please provide valid input");
      throw new WalletException("Please provide valid input", HttpStatus.BAD_REQUEST);
    }
    if (inputModel.getAmount() <= 0) {
      log.error("Entered amount must be positive for user id : {}", inputModel.getSenderUserId());
      throw new WalletException("Please enter positive amount", HttpStatus.BAD_REQUEST);
    }
    Wallet sederWallet = walletRepo.findByUserId(inputModel.getSenderUserId());
    if (sederWallet == null) {
      log.error("Sender wallet details not found for user id : {}", inputModel.getSenderUserId());
      throw new WalletException("Sender wallet details not found", HttpStatus.NOT_FOUND);
    }
    Wallet receiverWallet = walletRepo.findByUserId(inputModel.getReceiverUserId());
    if (receiverWallet == null) {
      log.error("Receiver wallet details not found for user id : {}", inputModel.getReceiverUserId());
      throw new WalletException("Receiver wallet details not found", HttpStatus.NOT_FOUND);
    }
    final Integer updatedSenderBalance = sederWallet.getBalance() - inputModel.getAmount();
    if(updatedSenderBalance < 0) {
      log.error("Incorrect amount {}, Please check your balance before transaction : ", inputModel.getAmount());
      throw new WalletException("Incorrect amount", HttpStatus.BAD_REQUEST);
    }
    sederWallet.setBalance(updatedSenderBalance);
    sederWallet.setLastUpdatedDate(DateUtility.getTime());
    Transaction transaction = transactionObj(inputModel.getAmount(), sederWallet.getUser(), receiverWallet.getUser());

    final Integer updatedReceiverBalance = receiverWallet.getBalance() + inputModel.getAmount();
    receiverWallet.setBalance(updatedReceiverBalance);
    receiverWallet.setLastUpdatedDate(DateUtility.getTime());

    transactionRepo.save(transaction);
    walletRepo.save(sederWallet);
    walletRepo.save(receiverWallet);

    return new ResponseModel("Money send successfully");
  }

  private Transaction transactionObj(int amount, User senderUser, User receiverUser) {
    Transaction senderTransaction = new Transaction();
    senderTransaction.setTransactionMode(TransactionMode.WALLET);
    senderTransaction.setTransactionTime(DateUtility.getTime());
    senderTransaction.setSenderUser(senderUser);
    senderTransaction.setReceiverUser(receiverUser);
    senderTransaction.setAmount(amount);
    return senderTransaction;
  }

  @Override
  public ResponseModel getPassbook(Integer userId) {
    PassbookResponseModel responseModel = new PassbookResponseModel();
    List<Transaction> transactionList = transactionRepo.findAllBySenderUserIdOrReceiverUserId(userId, userId);
    if (transactionList == null || transactionList.isEmpty()) {
      log.error("No transaction found for user id : {}", userId);
      throw new WalletException("No transaction found", HttpStatus.NOT_FOUND);
    }
    List<PassbookDetails> passbookDetailsList;
    try {
      passbookDetailsList = transactionList.parallelStream()
              .map(obj -> PassbookDetails.builder()
                      .amount(obj.getAmount())
                      .mode(obj.getTransactionMode())
                      .type(obj.getTransactionMode().equals(TransactionMode.UPI) ?
                              TransactionType.CREDIT : obj.getReceiverUser().getId() == userId ? TransactionType.CREDIT : TransactionType.DEBIT)
                      .targetUsername(obj.getTransactionMode().equals(TransactionMode.WALLET) ?
                              obj.getReceiverUser().getId() == userId ? obj.getSenderUser().getUsername() : obj.getReceiverUser().getUsername() : null)
                      .transactionTime(obj.getTransactionTime())
                      .build())
              .sorted(Comparator.comparing(PassbookDetails::getTransactionTime))
              .collect(Collectors.toList());
    } catch (Exception ex){
      log.error("Fail to process the data for user id : {}", userId);
      throw new WalletException("Fail to process the data", HttpStatus.EXPECTATION_FAILED);
    }
    responseModel.setPassbookDetailsList(passbookDetailsList);
    return new ResponseModel(responseModel, "Transaction fetch successfully");
  }

}









