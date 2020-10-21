package cyyGroup.cyyArt.order.vo;

import java.util.List;
import java.util.UUID;

import cyyGroup.cyyArt.until.HttpUntil;

public class SubmitSource {
	private String bizPlatform;
	private String bookKey;
	private String clientIp;
	private boolean fromThirdApp;

	private List<SubmitItemSource> itemSources;
	private String kdtSessionId;
	private boolean needAppRedirect;
	private String orderFrom;
	private Integer orderType;
	private String platform;
	private String salesman;
	private String userAgent;

	public SubmitSource() throws Exception {
		this.bizPlatform = "";
		this.bookKey = UUID.randomUUID().toString();
		this.clientIp = HttpUntil.getLocalhost(2);
		this.fromThirdApp = false;
		this.needAppRedirect = false;
		this.orderFrom = "cart";
		this.orderType = 0;
		this.platform = "mobile";
		this.salesman = "";
		this.userAgent = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Mobile Safari/537.36";
	}

	public String getBizPlatform() {
		return bizPlatform;
	}

	public void setBizPlatform(String bizPlatform) {
		this.bizPlatform = bizPlatform;
	}

	public String getBookKey() {
		return bookKey;
	}

	public void setBookKey(String bookKey) {
		this.bookKey = bookKey;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public boolean isFromThirdApp() {
		return fromThirdApp;
	}

	public void setFromThirdApp(boolean fromThirdApp) {
		this.fromThirdApp = fromThirdApp;
	}

	public List<SubmitItemSource> getItemSources() {
		return itemSources;
	}

	public void setItemSources(List<SubmitItemSource> itemSources) {
		this.itemSources = itemSources;
	}

	public String getKdtSessionId() {
		return kdtSessionId;
	}

	public void setKdtSessionId(String kdtSessionId) {
		this.kdtSessionId = kdtSessionId;
	}

	public boolean isNeedAppRedirect() {
		return needAppRedirect;
	}

	public void setNeedAppRedirect(boolean needAppRedirect) {
		this.needAppRedirect = needAppRedirect;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}
