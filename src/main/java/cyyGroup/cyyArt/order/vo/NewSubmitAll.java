package cyyGroup.cyyArt.order.vo;

import java.util.List;

public class NewSubmitAll {
	private String area;
	private String city;
	private String houseNumber;
	private String postFee = "0";
	private String province;
	private String receiverName;
	private String receiverPhone;
	private String remark = "";
	private String street = "";
	private String price;

	public String getPostFee() {
		return postFee;
	}

	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	private List<NewSubmitGood> goodsList;
	private List<DiscountGood> discountGoodsList;

	public List<DiscountGood> getDiscountGoodsList() {
		return discountGoodsList;
	}

	public void setDiscountGoodsList(List<DiscountGood> discountGoodsList) {
		this.discountGoodsList = discountGoodsList;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public List<NewSubmitGood> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<NewSubmitGood> goodsList) {
		this.goodsList = goodsList;
	}

}
