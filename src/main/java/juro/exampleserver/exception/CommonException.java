package juro.exampleserver.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class CommonException extends RuntimeException {
	private ErrorCode errorCode;
	private String debugMessage;

	public CommonException(ErrorCode errorCode, String debugMessage) {
		super(debugMessage);
		this.errorCode = errorCode;
	}

	public CommonException(ErrorCode errorCode, String debugMessage, Throwable e) {
		super(debugMessage, e);
		this.errorCode = errorCode;
		this.debugMessage = debugMessage;
	}

}
