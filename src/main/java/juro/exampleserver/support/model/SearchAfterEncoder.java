package juro.exampleserver.support.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.exception.ServerException;

/**
 * searchAfter encoder
 * 정렬 키가 2개 이상일 경우 다음 값 또한 2개의 필드를 나타내야만 함
 * 클라이언트 입장에서 정렬 키를 알 필요 없음
 * 다음 값만 조회할 수 있도록 String으로 인코딩하여 전달한다.
 */
public class SearchAfterEncoder {

	/**
	 *
	 * @param values 정렬 키 목록
	 * @return encoded 인코딩된 값. searchAfter에 해당
	 */
	public static String encode(String... values) {
		if (values == null || values.length == 0) {
			return null;
		}

		String collect = String.join(";", values);
		return Base64.getUrlEncoder().encodeToString(collect.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 *
	 * {@code decode(String)} 의 편의 메서드.정렬 키가 1개인 경우 사용
	 *
	 * @param encoded 인코딩된 값. searchAfter에 해당
	 * @return value 디코딩된 값
	 * @see     #decode(String)
	 */
	public static String decodeSingle(String encoded) {
		String[] decode = decode(encoded);
		if (decode.length != 1) {
			throw new ServerException(ErrorCode.INTERNAL_SERVER_ERROR,
				"Invalid encoded string. it must contain one : " + encoded);
		}

		return decode[0];
	}
	/**
	 *
	 *
	 * @param encoded 인코딩된 값. searchAfter에 해당
	 * @return values 디코딩된 값. 정렬키 목록에 해당
	 */
	public static String[] decode(String encoded) {
		byte[] decode = Base64.getUrlDecoder().decode(encoded);
		String collect = new String(decode, StandardCharsets.UTF_8);

		return collect.split(";");
	}
}
