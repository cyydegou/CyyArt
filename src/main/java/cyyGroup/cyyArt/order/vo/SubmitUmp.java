package cyyGroup.cyyArt.order.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitUmp {
	private List<SubmitActivitie> activities;
	private Map<String, String> coupon;
	private SubmitUseCustomerCardInfo useCustomerCardInfo;

	public SubmitUmp() {
		this.coupon = new HashMap<String, String>();
	}

	public List<SubmitActivitie> getActivities() {
		return activities;
	}

	public void setActivities(List<SubmitActivitie> activities) {
		this.activities = activities;
	}

	public Map<String, String> getCoupon() {
		return coupon;
	}

	public void setCoupon(Map<String, String> coupon) {
		this.coupon = coupon;
	}

	public SubmitUseCustomerCardInfo getUseCustomerCardInfo() {
		return useCustomerCardInfo;
	}

	public void setUseCustomerCardInfo(SubmitUseCustomerCardInfo useCustomerCardInfo) {
		this.useCustomerCardInfo = useCustomerCardInfo;
	}

}
