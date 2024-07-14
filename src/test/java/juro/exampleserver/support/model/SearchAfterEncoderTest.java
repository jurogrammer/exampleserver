package juro.exampleserver.support.model;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import juro.exampleserver.exception.ServerException;

class SearchAfterEncoderTest {

	@Test
	void testEncodeWithMultipleValues() {
		String encoded = SearchAfterEncoder.encode("value1", "value2");
		String expected = Base64.getUrlEncoder().encodeToString("value1;value2".getBytes(StandardCharsets.UTF_8));

		assertThat(encoded).isEqualTo(expected);
	}

	@Test
	void testEncodeWithSingleValue() {
		String encoded = SearchAfterEncoder.encode("value1");
		String expected = Base64.getUrlEncoder().encodeToString("value1".getBytes(StandardCharsets.UTF_8));

		assertThat(encoded).isEqualTo(expected);
	}

	@Test
	void testEncodeWithNoValues() {
		String encoded = SearchAfterEncoder.encode();

		assertThat(encoded).isNull();
	}

	@Test
	void testEncodeWithNullValues() {
		String encoded = SearchAfterEncoder.encode((String[])null);

		assertThat(encoded).isNull();
	}

	@Test
	void testDecodeSingleWithValidEncodedString() {
		String encoded = Base64.getUrlEncoder().encodeToString("value1".getBytes(StandardCharsets.UTF_8));
		String decoded = SearchAfterEncoder.decodeSingle(encoded);

		assertThat(decoded).isEqualTo("value1");
	}

	@Test
	void testDecodeSingleWithInvalidEncodedString() {
		String encoded = Base64.getUrlEncoder().encodeToString("value1;value2".getBytes(StandardCharsets.UTF_8));

		assertThatThrownBy(() -> SearchAfterEncoder.decodeSingle(encoded))
			.isInstanceOf(ServerException.class)
			.hasMessageContaining("Invalid encoded string. it must contain one : " + encoded);
	}

	@Test
	void testDecodeWithValidEncodedString() {
		String encoded = Base64.getUrlEncoder().encodeToString("value1;value2".getBytes(StandardCharsets.UTF_8));
		String[] decoded = SearchAfterEncoder.decode(encoded);

		assertThat(decoded).containsExactly("value1", "value2");
	}

	@Test
	void testDecodeWithSingleValue() {
		String encoded = Base64.getUrlEncoder().encodeToString("value1".getBytes(StandardCharsets.UTF_8));
		String[] decoded = SearchAfterEncoder.decode(encoded);

		assertThat(decoded).containsExactly("value1");
	}

	@Test
	void testDecodeWithEmptyString() {
		String encoded = Base64.getUrlEncoder().encodeToString("".getBytes(StandardCharsets.UTF_8));
		String[] decoded = SearchAfterEncoder.decode(encoded);

		assertThat(decoded).containsExactly("");
	}
}
