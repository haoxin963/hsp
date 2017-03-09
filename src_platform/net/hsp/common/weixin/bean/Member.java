package net.hsp.common.weixin.bean;

public class Member extends User {
	private String userId;
	private String userPhone;
	private String bindBank;
	private String payPwd;
	private String accountId;

	private String bwaletTotal = "0.00";
	private String waletTotal = "0.00";
	private String walletAmt = "0.00";
	
 

	public String getBwaletTotal() {
		return bwaletTotal;
	}

	public void setBwaletTotal(String bwaletTotal) {
		this.bwaletTotal = bwaletTotal;
	}

	public String getWaletTotal() {
		return waletTotal;
	}

	public void setWaletTotal(String waletTotal) {
		this.waletTotal = waletTotal;
	}

	public String getWalletAmt() {
		return walletAmt;
	}

	public void setWalletAmt(String walletAmt) {
		this.walletAmt = walletAmt;
	}

	public String getBindBank() {
		return bindBank;
	}

	public void setBindBank(String bindBank) {
		this.bindBank = bindBank;
	}
 
	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
}
