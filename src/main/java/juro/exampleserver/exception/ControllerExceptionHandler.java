package juro.exampleserver.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import juro.exampleserver.controller.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ClientException.class)
	public ResponseEntity<ApiResponse<Void>> handleServerException(
		ClientException exception
	) {
		log.warn("클라이언트 에러 발생", exception);

		ApiResponse<Void> response = ApiResponse.fail(exception.getErrorCode());
		return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception
	) {
		log.warn("클라이언트 에러 발생", exception);

		ErrorCode unknownExceptionErrorCode = ErrorCode.BAD_REQUEST;
		// TODO: invlaid 값 보고 적당히 클라에게 메세지 보내기
		ApiResponse<Void> response = ApiResponse.fail(unknownExceptionErrorCode);
		return ResponseEntity.status(unknownExceptionErrorCode.getHttpStatus()).body(response);
	}

	@ExceptionHandler(ServerException.class)
	public ResponseEntity<ApiResponse<Void>> handleServerException(
		ServerException exception
	) {
		log.error("내부 서버 에러 발생", exception);

		ApiResponse<Void> response = ApiResponse.fail(exception.getErrorCode());
		return ResponseEntity.status(exception.getErrorCode().getHttpStatus()).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleServerException(
		Exception exception
	) {
		log.error("알 수 없는 에러 발생", exception);

		ErrorCode unknownExceptionErrorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		ApiResponse<Void> response = ApiResponse.fail(unknownExceptionErrorCode);
		return ResponseEntity.status(unknownExceptionErrorCode.getHttpStatus()).body(response);
	}
}
