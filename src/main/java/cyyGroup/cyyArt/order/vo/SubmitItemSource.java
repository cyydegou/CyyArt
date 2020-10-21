package cyyGroup.cyyArt.order.vo;

public class SubmitItemSource {
	private Integer activityId;
	private Integer activityType;
	private Long cartCreateTime;
	private Long cartUpdateTime;
	private Integer goodsId;
	private Integer skuId;

	public SubmitItemSource() {
		this.activityId = 0;
		this.activityType = 1;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getActivityType() {
		return activityType;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
	}

	public Long getCartCreateTime() {
		return cartCreateTime;
	}

	public void setCartCreateTime(Long cartCreateTime) {
		this.cartCreateTime = cartCreateTime;
	}

	public Long getCartUpdateTime() {
		return cartUpdateTime;
	}

	public void setCartUpdateTime(Long cartUpdateTime) {
		this.cartUpdateTime = cartUpdateTime;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

}
