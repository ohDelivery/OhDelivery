package com.ohdelivery.service.user.application.exception;

import com.ohdelivery.common.exception.BaseException;
import com.ohdelivery.common.exception.ErrorCode;

public class UserException {

    public static class UserNotFoundException extends BaseException {

        public UserNotFoundException() {
            super(ErrorCode.USER_NOT_FOUND_EXCEPTION);
        }
    }

    public static class UserAlreadyExistsException extends BaseException {
        public UserAlreadyExistsException() {
            super(ErrorCode.ALREADY_EXIST_USER_EXCEPTION);
        }
    }

    public static class IncorrectPasswordException extends BaseException {
        public IncorrectPasswordException() {
            super(ErrorCode.INCORRECT_PASSWORD_EXCEPTION);
        }
    }
}
