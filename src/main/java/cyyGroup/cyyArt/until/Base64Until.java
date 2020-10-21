package cyyGroup.cyyArt.until;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Until {

	/**
	 * 编码
	 * 
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encoded(String text) throws UnsupportedEncodingException {
		final Base64 base64 = new Base64();
		final byte[] textByte = text.getBytes("UTF-8");
		// 编码
		final String encodedText = base64.encodeToString(textByte);
		return encodedText;
	}

	/**
	 * 解码
	 * 
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String text) throws UnsupportedEncodingException {
		final Base64 base64 = new Base64();

		// 解码
		return new String(base64.decode(text), "UTF-8");

	}
}
