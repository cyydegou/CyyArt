package cyyGroup.cyyArt.order.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitOrder {
	private Map<String, Map<String, String>> cloudOrderExt;

	private SubmitConfig config;

	private SubmitDelivery delivery;

	private List<SubmitItem> items;

	private SubmitSeller seller;

	private SubmitSource source;

	private SubmitUmp ump;

	private List<String> unavailableItems;

	private Map<String, String> usePayAsset;

	public SubmitOrder() {
		this.cloudOrderExt = new HashMap<String, Map<String, String>>();
		Map<String, String> tempMap = new HashMap<String, String>();
		cloudOrderExt.put("extension", tempMap);
		this.unavailableItems = new ArrayList<String>();
		this.usePayAsset = new HashMap<String, String>();
	}

	public List<String> getUnavailableItems() {
		return unavailableItems;
	}

	public void setUnavailableItems(List<String> unavailableItems) {
		this.unavailableItems = unavailableItems;
	}

	public Map<String, String> getUsePayAsset() {
		return usePayAsset;
	}

	public void setUsePayAsset(Map<String, String> usePayAsset) {
		this.usePayAsset = usePayAsset;
	}

	public SubmitSource getSource() {
		return source;
	}

	public void setSource(SubmitSource source) {
		this.source = source;
	}

	public SubmitUmp getUmp() {
		return ump;
	}

	public void setUmp(SubmitUmp ump) {
		this.ump = ump;
	}

	public SubmitSeller getSeller() {
		return seller;
	}

	public void setSeller(SubmitSeller seller) {
		this.seller = seller;
	}

	public List<SubmitItem> getItems() {
		return items;
	}

	public void setItems(List<SubmitItem> items) {
		this.items = items;
	}

	public SubmitDelivery getDelivery() {
		return delivery;
	}

	public void setDelivery(SubmitDelivery delivery) {
		this.delivery = delivery;
	}

	public SubmitConfig getConfig() {
		return config;
	}

	public void setConfig(SubmitConfig config) {
		this.config = config;
	}

	public Map<String, Map<String, String>> getCloudOrderExt() {
		return cloudOrderExt;
	}

	public void setCloudOrderExt(Map<String, Map<String, String>> cloudOrderExt) {
		this.cloudOrderExt = cloudOrderExt;
	}

}
