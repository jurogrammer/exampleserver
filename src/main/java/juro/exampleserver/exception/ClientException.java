package juro.exampleserver.exception;

public class ClientException extends CommonException {
	public ClientException(ErrorCode errorCode, String debugMessage) {
		super(errorCode, debugMessage);
	}

	public ClientException(ErrorCode errorCode, String debugMessage, Throwable e) {
		super(errorCode, debugMessage, e);
	}
}
