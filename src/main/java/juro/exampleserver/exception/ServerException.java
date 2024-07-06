package juro.exampleserver.exception;

public class ServerException extends CommonException {

	public ServerException(ErrorCode errorCode, String debugMessage) {
		super(errorCode, debugMessage);
	}

	public ServerException(ErrorCode errorCode, String debugMessage, Throwable e) {
		super(errorCode, debugMessage, e);
	}
}
