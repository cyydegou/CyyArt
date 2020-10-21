package cyyGroup.cyyArt.money.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cyyGroup.cyyArt.vo.Money;

@Mapper
public interface MoneyDao {
	/**
	 * 得到所有的钱列表
	 * 
	 * @return
	 */
	List<Money> getMoneys();
}
