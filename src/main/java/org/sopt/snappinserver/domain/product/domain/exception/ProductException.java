package org.sopt.snappinserver.domain.product.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class ProductException extends BusinessException {
  public ProductException(ProductErrorCode productErrorCode) {
    super(productErrorCode);
  }
}
