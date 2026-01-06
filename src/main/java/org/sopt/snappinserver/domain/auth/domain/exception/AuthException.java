package org.sopt.snappinserver.domain.auth.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class AuthException extends BusinessException {
  public AuthException(AuthErrorCode authErrorCode) {
    super(authErrorCode);
  }
}
