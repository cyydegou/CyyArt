package cyyGroup.cyyArt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cyyGroup.cyyArt.money.service.MoneyService;
import cyyGroup.cyyArt.vo.Money;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private MoneyService moneyService;

	@RequestMapping("/test")
	public String test() {
		return "test";
	}

	@RequestMapping("/aa")
	public String aa() {
		return "aaa";
	}

	@RequestMapping("/getmoneylist")
	public @ResponseBody List<Money> getMoneyList() {
		return moneyService.getMoneyList();
	}

	@RequestMapping("/redis")
	public String Redis(@RequestParam String key, @RequestParam String para) {
		return moneyService.Redis(key, para);
	}
}
