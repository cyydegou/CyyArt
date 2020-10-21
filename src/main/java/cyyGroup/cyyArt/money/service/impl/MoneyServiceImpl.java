package cyyGroup.cyyArt.money.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cyyGroup.cyyArt.money.dao.MoneyDao;
import cyyGroup.cyyArt.money.service.MoneyService;
import cyyGroup.cyyArt.vo.Money;

@Service
public class MoneyServiceImpl implements MoneyService {

	@Autowired
	private MoneyDao moneyDao;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<Money> getMoneyList() {
		return moneyDao.getMoneys();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String Redis(String key, String para) {
		// Long increment = stringRedisTemplate.opsForValue().increment("test0724");

		// stringRedisTemplate.opsForList().leftPush(key, para);
		// List<String> leftPop = stringRedisTemplate.opsForList().range(key, 0, -1);

		Money m = new Money();
		m.setId(1);
		m.setMoney(10);
		redisTemplate.opsForValue().set(key, m);
		Object object = redisTemplate.opsForValue().get(key);
		String json = JSONObject.toJSONString(object);
		return json;
	}

}
