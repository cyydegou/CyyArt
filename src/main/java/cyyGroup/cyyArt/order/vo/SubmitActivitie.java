package cyyGroup.cyyArt.order.vo;

public class SubmitActivitie {
	private String activityAlias;
	private Integer activityId;
	private Integer activityType;
	private Integer externalPointId;
	private Integer goodsId;
	private Integer kdtId;
	private Integer pointsPrice;
	private Integer skuId;
	private boolean usePoints;

	public SubmitActivitie() {
		this.activityAlias = "";
		this.activityId = 0;
		this.activityType = 1;
		this.externalPointId = 0;
		this.pointsPrice = 0;
		this.usePoints = false;
	}

	public String getActivityAlias() {
		return activityAlias;
	}

	public void setActivityAlias(String activityAlias) {
		this.activityAlias = activityAlias;
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

	public Integer getExternalPointId() {
		return externalPointId;
	}

	public void setExternalPointId(Integer externalPointId) {
		this.externalPointId = externalPointId;
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

	public Integer getPointsPrice() {
		return pointsPrice;
	}

	public void setPointsPrice(Integer pointsPrice) {
		this.pointsPrice = pointsPrice;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public boolean isUsePoints() {
		return usePoints;
	}

	public void setUsePoints(boolean usePoints) {
		this.usePoints = usePoints;
	}

}
