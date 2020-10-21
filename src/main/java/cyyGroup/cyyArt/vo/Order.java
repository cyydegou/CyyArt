package cyyGroup.cyyArt.vo;

import java.io.Serializable;

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	private Integer id;
	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品下单时间
	 */
	private String createTime;

	/**
	 * 商品图片
	 */
	private String picUrl;

	/**
	 * 商品发货时间
	 */
	private String sendTime;

	private User user;

	private String color;
	/**
	 * 1均码 2S 3M 4L 5XL 5XL
	 */
	private Integer size;

	private String remark;

	private String baiId;

	private String kdt_id;

	private Integer status;

	private String expressUrl;

	private String orderDescribe;

	private Integer num;

	private Integer pay;

	private String money;

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Integer getPay() {
		return pay;
	}

	public void setPay(Integer pay) {
		this.pay = pay;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getOrderDescribe() {
		return orderDescribe;
	}

	public void setOrderDescribe(String orderDescribe) {
		this.orderDescribe = orderDescribe;
	}

	public String getExpressUrl() {
		return expressUrl;
	}

	public void setExpressUrl(String expressUrl) {
		this.expressUrl = expressUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKdt_id() {
		return kdt_id;
	}

	public void setKdt_id(String kdt_id) {
		this.kdt_id = kdt_id;
	}

	public String getBaiId() {
		return baiId;
	}

	public void setBaiId(String baiId) {
		this.baiId = baiId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
