package com.bishe.aapay.dto;

import java.io.Serializable;

public class Payment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	/*
	 * ĳ�����ѵı��
	 */
	private long consumptionId;
	/*
	 * ������Ա��ID
	 */
	private long partId;
	/*
	 * �渶��
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
