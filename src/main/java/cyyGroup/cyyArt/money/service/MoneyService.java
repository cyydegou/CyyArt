package cyyGroup.cyyArt.money.service;

import java.util.List;

import cyyGroup.cyyArt.vo.Money;

public interface MoneyService {

	/**
	 * 得到所有钱列表
	 * 
	 * @return
	 */
	List<Money> getMoneyList();

	/**
	 * 测试各种redis功能
	 * 
	 * @param para
	 * @param para2
	 * 
	 * @return
	 */
	String Redis(String key, String para);

}
