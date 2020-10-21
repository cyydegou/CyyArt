package cyyGroup.cyyArt.order.vo;

import java.util.HashMap;

public class SubmitItem {
	private Integer activityId;
	private Integer activityType;
	private Integer deliverTime;
	private HashMap<String, String> extensions;
	private Integer goodsId;
	private Integer kdtId;
	private Integer num;
	private Integer skuId;

	public SubmitItem() {
		this.activityId = 0;
		this.activityType = 1;
		this.deliverTime = 0;
		this.extensions = new HashMap<String, String>();
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

	public Integer getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Integer deliverTime) {
		this.deliverTime = deliverTime;
	}

	public HashMap<String, String> getExtensions() {
		return extensions;
	}

	public void setExtensions(HashMap<String, String> extensions) {
		this.extensions = extensions;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getKdtId() {
		return kdtId;
	}

	public void setKdtId(Integer kdtId) {
		this.kdtId = kdtId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

}
