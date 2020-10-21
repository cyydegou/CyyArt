package cyyGroup.cyyArt.vo;

import java.util.List;

public class Express {
	private String expressCompanyName;
	private String expressNo;
	private String expressStatus;
	private List<ExpressProgress> expressProgress;

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getExpressStatus() {
		return expressStatus;
	}

	public void setExpressStatus(String expressStatus) {
		this.expressStatus = expressStatus;
	}

	public List<ExpressProgress> getExpressProgress() {
		return expressProgress;
	}

	public void setExpressProgress(List<ExpressProgress> expressProgress) {
		this.expressProgress = expressProgress;
	}

}
