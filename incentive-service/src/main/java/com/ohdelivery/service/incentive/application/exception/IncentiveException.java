package com.ohdelivery.service.incentive.application.exception;

import com.ohdelivery.common.exception.BaseException;
import com.ohdelivery.common.exception.ErrorCode;

public class IncentiveException extends BaseException{
	public IncentiveException(ErrorCode errorCode) {
		super(errorCode);
	}
	public static class IncentiveNotFoundException extends IncentiveException {
		public IncentiveNotFoundException() {
			super(ErrorCode.INCENTIVE_NOT_FOUND_EXCEPTION);
		}
	}
}
