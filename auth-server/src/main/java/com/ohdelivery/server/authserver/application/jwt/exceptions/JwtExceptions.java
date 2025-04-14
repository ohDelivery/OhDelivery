package com.ohdelivery.server.authserver.application.jwt.exceptions;

import com.ohdelivery.common.exception.BaseException;
import com.ohdelivery.common.exception.ErrorCode;

public class JwtExceptions {

    public static class JwtExpiredException extends BaseException{
        public JwtExpiredException() {
            super(ErrorCode.EXPIRED_TOKEN_EXCEPTION);
        }
    }

    public static class JwtInvalidException extends BaseException{
        public JwtInvalidException() {
            super(ErrorCode.INVALID_TOKEN_EXCEPTION);
        }

        public JwtInvalidException(String message) {
            super(ErrorCode.INVALID_TOKEN_EXCEPTION, message);
        }
    }

}
