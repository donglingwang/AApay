package com.bishe.aapay.dto;

import java.io.Serializable;

public class SettlementDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//参加活动的总的垫付额
	private double totalAdvanceMoney;
	//总的欠钱
	private double totalOweMoney;
	//总的应付金额
	private double totalMoney;
	public double getTotalAdvanceMoney() {
		return totalAdvanceMoney;
	}
	public void setTotalAdvanceMoney(double totalAdvanceMoney) {
		this.totalAdvanceMoney = totalAdvanceMoney;
	}
	public double getTotalOweMoney() {
		return totalOweMoney;
	}
	public void setTotalOweMoney(double totalOweMoney) {
		this.totalOweMoney = totalOweMoney;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	
}
