package com.bishe.aapay.dto;

import java.io.Serializable;

public class SettlementDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//�μӻ���ܵĵ渶��
	private double totalAdvanceMoney;
	//�ܵ�ǷǮ
	private double totalOweMoney;
	//�ܵ�Ӧ�����
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
