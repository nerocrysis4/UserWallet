package com.user.wallet.model.state;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
  ADMIN,
  CLIENT;

  public String getAuthority() {
    return name();
  }

}
