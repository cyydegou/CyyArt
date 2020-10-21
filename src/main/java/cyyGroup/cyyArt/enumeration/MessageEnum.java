package cyyGroup.cyyArt.enumeration;

public enum MessageEnum {

	SUCCESS(1, "操作成功"), FAIL(2, "操作失败"), ERRORPASS(3, "密码错误"), NOTLOGIN(4, "未登录"), BAI_NOTLOGIN(5,
	        "白秋节那里未登录"), SYN_USER_FIR(6,
	                "先同步用户"), INPUTMONEY(7, "请输入金额"), NEW_NOTLOGIN(8, "新平台登录失效"), USER_NOTEXIST(9, "新平台账号不存在");

	/**
	 * 信息
	 */
	public final String message;
	/**
	 * 代号码
	 */
	public final int code;

	MessageEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

}
