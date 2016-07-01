package com.bishe.aapay.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import android.R.integer;
/**
 * 一次消费的POJO
 * @author DongLing
 *
 */
public class Consumption implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*编号*/
	private long id;
	/*名称*/
	private String name;
	/*团队ID*/
	private long teamId;
	/*团队名称*/
	private String teamName;
	/*消费方式*/
	private String consumptionMode;
	/*消费金额*/
	private double money;
	/*消费类型*/
	private String type;
	/*消费时间*/
	private String time;
	/*参与人员*/
	private List<Map<String, String>> participantList;
	
	private int participantNum;
	/*备注*/
	private String remark;
	/*是否还清*/
	private String payOFF;
	private String operateTime;
	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public Consumption() {
		// TODO Auto-generated constructor stub
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getConsumptionMode() {
		return consumptionMode;
	}
	public void setConsumptionMode(String consumptionMode) {
		this.consumptionMode = consumptionMode;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public List<Map<String, String>> getParticipantList() {
		return participantList;
	}
	public void setParticipantList(List<Map<String, String>> participantList) {
		this.participantList = participantList;
	}
	public int getParticipantNum() {
		return participantNum;
	}
	public void setParticipantNum(int participantNum) {
		this.participantNum = participantNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPayOFF() {
		return payOFF;
	}
	public void setPayOFF(String payOFF) {
		this.payOFF = payOFF;
	}
	@Override
	public String toString() {
		return "Consumption [id=" + id + ", name=" + name
				+ ", teamName=" + teamName + ", consumptionMode="
				+ consumptionMode + ", money=" + money + ", type=" + type
				+ ", time=" + time + ", participantList=" + participantList
				+ ", remark=" + remark + "]";
	}

}
