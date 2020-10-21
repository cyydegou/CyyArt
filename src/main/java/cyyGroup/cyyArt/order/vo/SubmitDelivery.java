package cyyGroup.cyyArt.order.vo;

public class SubmitDelivery {
	private String expressType;
	private Integer expressTypeChoice;
	private boolean hasFreightInsurance;
	private SubmitAddress address;

	public SubmitDelivery() {
		this.expressType = "express";
		this.expressTypeChoice = 0;
		this.hasFreightInsurance = false;
	}

	public String getExpressType() {
		return expressType;
	}

	public void setExpressType(String expressType) {
		this.expressType = expressType;
	}

	public Integer getExpressTypeChoice() {
		return expressTypeChoice;
	}

	public void setExpressTypeChoice(Integer expressTypeChoice) {
		this.expressTypeChoice = expressTypeChoice;
	}

	public boolean isHasFreightInsurance() {
		return hasFreightInsurance;
	}

	public void setHasFreightInsurance(boolean hasFreightInsurance) {
		this.hasFreightInsurance = hasFreightInsurance;
	}

	public SubmitAddress getAddress() {
		return address;
	}

	public void setAddress(SubmitAddress address) {
		this.address = address;
	}

}
