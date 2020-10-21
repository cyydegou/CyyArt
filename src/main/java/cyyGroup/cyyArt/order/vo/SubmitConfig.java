package cyyGroup.cyyArt.order.vo;

public class SubmitConfig {
	private String buyerMsg;
	private boolean containsUnavailableItems;
	private Integer paymentExpiry;
	private boolean receiveMsg;
	private boolean usePoints;
	private boolean useWxpay;

	public SubmitConfig() {
		this.buyerMsg = "";
		this.containsUnavailableItems = false;
		this.paymentExpiry = 0;
		this.receiveMsg = true;
		this.usePoints = false;
		this.useWxpay = false;
	}

	public String getBuyerMsg() {
		return buyerMsg;
	}

	public void setBuyerMsg(String buyerMsg) {
		this.buyerMsg = buyerMsg;
	}

	public boolean isContainsUnavailableItems() {
		return containsUnavailableItems;
	}

	public void setContainsUnavailableItems(boolean containsUnavailableItems) {
		this.containsUnavailableItems = containsUnavailableItems;
	}

	public Integer getPaymentExpiry() {
		return paymentExpiry;
	}

	public void setPaymentExpiry(Integer paymentExpiry) {
		this.paymentExpiry = paymentExpiry;
	}

	public boolean isReceiveMsg() {
		return receiveMsg;
	}

	public void setReceiveMsg(boolean receiveMsg) {
		this.receiveMsg = receiveMsg;
	}

	public boolean isUsePoints() {
		return usePoints;
	}

	public void setUsePoints(boolean usePoints) {
		this.usePoints = usePoints;
	}

	public boolean isUseWxpay() {
		return useWxpay;
	}

	public void setUseWxpay(boolean useWxpay) {
		this.useWxpay = useWxpay;
	}

}
