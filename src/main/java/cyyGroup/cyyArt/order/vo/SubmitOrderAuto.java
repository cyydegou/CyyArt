package cyyGroup.cyyArt.order.vo;

import cyyGroup.cyyArt.vo.ShoppingCart;

public class SubmitOrderAuto {
	private ShoppingCart shoppingCart;
	private Integer goodId;
	private Integer sku_id;
	private String sessionId;
	private Long startSoldTime;
	private String url;
	// private Integer price;
	/**
	 * 一个重要统一的id
	 */
	private Integer kdt_id;

	public Integer getKdt_id() {
		return kdt_id;
	}

	public void setKdt_id(Integer kdt_id) {
		this.kdt_id = kdt_id;
	}

	// public Integer getPrice() {
	// return price;
	// }

	// public void setPrice(Integer price) {
	// this.price = price;
	// }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getStartSoldTime() {
		return startSoldTime;
	}

	public void setStartSoldTime(Long startSoldTime) {
		this.startSoldTime = startSoldTime;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public Integer getGoodId() {
		return goodId;
	}

	public void setGoodId(Integer goodId) {
		this.goodId = goodId;
	}

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
