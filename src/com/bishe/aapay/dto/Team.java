package com.bishe.aapay.dto;

import java.io.Serializable;
import java.util.List;

public class Team implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int teamId;
	private String teamName;
	private List<Participant> participants;
	private int teamNum;
	private double consumeTotalAmount;
	private String foundDate;
	private String hasPart;
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
	public int getTeamNum() {
		return teamNum;
	}
	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}
	public double getConsumeTotalAmount() {
		return consumeTotalAmount;
	}
	public void setConsumeTotalAmount(double consumeTotalAmount) {
		this.consumeTotalAmount = consumeTotalAmount;
	}
	public String getFoundDate() {
		return foundDate;
	}
	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}
	public String getHasPart() {
		return hasPart;
	}
	public void setHasPart(String hasPart) {
		this.hasPart = hasPart;
	}
	@Override
	public String toString() {
		return "Team [teamId=" + teamId + ", teamName=" + teamName
				+ ", participants=" + participants + ", consumeTotalAmount="
				+ consumeTotalAmount + "]";
	}
}
