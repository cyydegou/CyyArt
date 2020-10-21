/**
 * @description 类描述
 * @author "caiyy"
 * @date 2018年7月3日
 */
package cyyGroup.cyyArt.until;

import java.util.List;

/**
 * @description 类描述
 * @author "caiyy"
 * @date 2018年7月3日
 */
public class CommonUntil {

	public static boolean isEmptyStr(String s) {
		if (s == null || "".equals(s)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmptyList(List list) {
		if (list == null || list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
