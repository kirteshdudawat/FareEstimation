package com.kirtesh.fareestimation.exception;

import com.kirtesh.fareestimation.enums.ErrorCodes;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message = null;

	private ErrorCodes code;

	public ServiceException(ErrorCodes code) {
		super(code.message());
		this.code = code;
		this.message = code.message();
	}

	public ServiceException(ErrorCodes code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public ServiceException(ErrorCodes code, Throwable e) {
		super(code.message(), e);
		this.code = code;
		this.message = code.message();
	}

	public ServiceException(ErrorCodes code, String message, Throwable e) {
		super(message, e);
		this.code = code;
		this.message = message;
	}

	public ErrorCodes getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "ServiceException [message=" + message + ", code=" + code + "]";
	}

}
