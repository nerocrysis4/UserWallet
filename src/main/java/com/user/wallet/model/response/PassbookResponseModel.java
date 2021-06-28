package com.user.wallet.model.response;

import com.user.wallet.model.entity.Transaction;
import com.user.wallet.model.entity.User;
import com.user.wallet.model.state.TransactionMode;
import com.user.wallet.model.state.UserRole;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;

@Data
public class PassbookResponseModel {

  List<PassbookDetails> passbookDetailsList;

}
