package cyyGroup.cyyArt.money.service;

import cyyGroup.cyyArt.enumeration.MessageEnum;
import cyyGroup.cyyArt.until.CyyArtException;

public interface UserNewService {

	/**
	 * 测试逆向同步
	 */
	void testSyn();

	/**
	 * 同步新平台的顾客
	 * 
	 * @param integer
	 * 
	 * @throws Exception
	 */
	void synBuyer(Integer userId) throws Exception;

	/**
	 * 登录新平台
	 * 
	 * @param integer
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	MessageEnum loginBai(Integer belongId, String name, String password) throws CyyArtException, Exception;
}
