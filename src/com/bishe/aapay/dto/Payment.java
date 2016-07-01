package com.bishe.aapay.dto;

import java.io.Serializable;

public class Payment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	/*
	 * 某次消费的编号
	 */
	private long consumptionId;
	/*
	 * 参与人员的ID
	 */
	private long partId;
	/*
	 * 垫付额
	 */
	private double advanceMoney;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getConsumptionId() {
		return consumptionId;
	}
	public void setConsumptionId(long consumptionId) {
		this.consumptionId = consumptionId;
	}
	public long getPartId() {
		return partId;
	}
	public void setPartId(long partId) {
		this.partId = partId;
	}
	public double getAdvanceMoney() {
		return advanceMoney;
	}
	public void setAdvanceMoney(double advanceMoney) {
		this.advanceMoney = advanceMoney;
	}
	
	
	
}
