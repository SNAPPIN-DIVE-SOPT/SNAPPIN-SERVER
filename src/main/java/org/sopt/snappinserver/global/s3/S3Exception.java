package org.sopt.snappinserver.global.s3;

import org.sopt.snappinserver.global.exception.BusinessException;

public class S3Exception extends BusinessException {

    public S3Exception(S3ErrorCode s3ErrorCode) {
        super(s3ErrorCode);
    }
}
