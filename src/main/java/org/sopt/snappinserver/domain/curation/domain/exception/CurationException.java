package org.sopt.snappinserver.domain.curation.domain.exception;

import org.sopt.snappinserver.global.exception.BusinessException;

public class CurationException extends BusinessException {

    public CurationException(CurationErrorCode curationErrorCode) {
        super(curationErrorCode);
    }
}
