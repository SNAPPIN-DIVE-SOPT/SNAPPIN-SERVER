package org.sopt.snappinserver.domain.portfolio.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class PortfolioException extends BusinessException {

    public PortfolioException(PortfolioErrorCode portfolioErrorCode) {
        super(portfolioErrorCode);
    }
}
