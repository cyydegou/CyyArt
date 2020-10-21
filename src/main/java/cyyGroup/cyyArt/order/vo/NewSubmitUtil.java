package cyyGroup.cyyArt.order.vo;

import cyyGroup.cyyArt.vo.ShoppingCart;

public class NewSubmitUtil {

	private String mainGoodId;
	private String goodId;
	private ShoppingCart shoppingCart;
	private Long startSaleTime;
	private String image;
	private String name;
	private String spec1;
	private String price;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec1() {
		return spec1;
	}

	public void setSpec1(String spec1) {
		this.spec1 = spec1;
	}

	public Long getStartSaleTime() {
		return startSaleTime;
	}

	public void setStartSaleTime(Long startSaleTime) {
		this.startSaleTime = startSaleTime;
	}

	public String getMainGoodId() {
		return mainGoodId;
	}

	public void setMainGoodId(String mainGoodId) {
		this.mainGoodId = mainGoodId;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

}
