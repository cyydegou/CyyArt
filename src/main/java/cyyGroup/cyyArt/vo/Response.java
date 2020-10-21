package cyyGroup.cyyArt.vo;

import cyyGroup.cyyArt.enumeration.MessageEnum;

/**
 * 后台返回前台的数据
 * 
 * @description 类描述
 * @author "caiyy"
 * @date 2018年4月28日
 */
public class Response<T> {
	/**
	 * 是否成功 100:成功 200:失败
	 */
	private int code;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 数据实体
	 */
	private T data;

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	public Response(MessageEnum messageEnum) {
		this.code = messageEnum.getCode();
		this.message = messageEnum.getMessage();
	}
}
