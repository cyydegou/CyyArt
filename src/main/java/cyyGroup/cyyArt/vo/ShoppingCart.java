package cyyGroup.cyyArt.vo;

import java.io.Serializable;

public class ShoppingCart implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7025672710323484463L;

	/**
	 * 购买人id
	 */
	private Integer buyer_id;

	/**
	 * 规格id
	 */
	private Integer sku_id;
	/**
	 * 规格描述
	 */
	private String sku;

	/**
	 * 商品图片
	 */
	private String thumb_url;

	/**
	 * 商品id
	 */
	private Integer goods_id;

	/**
	 * 一个重要统一的id
	 */
	private Integer kdt_id;

	/**
	 * sesionid
	 */
	private String nobody;

	/**
	 * 商品名
	 */
	private String title;

	/**
	 * 准备时间
	 */
	private String grabTime;

	/**
	 * 准备时间
	 */
	private String createTime;

	private User user;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 购物车标识
	 */
	private String alias;

	private Long created_time;

	private Long updated_time;

	private Integer num;

	private String orderNo;

	private Integer id;

	private String remark;

	private int fromType = 1;

	private String goods_id_new;

	/**
	 * 准备时间：时
	 */
	private String grabTimeH;
	/**
	 * 准备时间：分
	 */
	private String grabTimeM;

	private Integer belongId;

	public Integer getBelongId() {
		return belongId;
	}

	public void setBelongId(Integer belongId) {
		this.belongId = belongId;
	}

	public String getGrabTimeH() {
		return grabTimeH;
	}

	public void setGrabTimeH(String grabTimeH) {
		this.grabTimeH = grabTimeH;
	}

	public String getGrabTimeM() {
		return grabTimeM;
	}

	public void setGrabTimeM(String grabTimeM) {
		this.grabTimeM = grabTimeM;
	}

	public String getGoods_id_new() {
		return goods_id_new;
	}

	public void setGoods_id_new(String goods_id_new) {
		this.goods_id_new = goods_id_new;
	}

	public int getFromType() {
		return fromType;
	}

	public void setFromType(int fromType) {
		this.fromType = fromType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Long getCreated_time() {
		return created_time;
	}

	public void setCreated_time(Long created_time) {
		this.created_time = created_time;
	}

	public Long getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(Long updated_time) {
		this.updated_time = updated_time;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getThumb_url() {
		return thumb_url;
	}

	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}

	public String getGrabTime() {
		return grabTime;
	}

	public void setGrabTime(String grabTime) {
		this.grabTime = grabTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getKdt_id() {
		return kdt_id;
	}

	public void setKdt_id(Integer kdt_id) {
		this.kdt_id = kdt_id;
	}

	public String getNobody() {
		return nobody;
	}

	public void setNobody(String nobody) {
		this.nobody = nobody;
	}

	public Integer getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(Integer buyer_id) {
		this.buyer_id = buyer_id;
	}

}
