package com.bishe.aapay.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.dto.Payment;
import com.bishe.aapay.dto.SettlementDTO;

public class ConsumptionService {

	private static ConsumptionService consumptionService;
	private ConsumptionService() {
		
	}
	
	public static ConsumptionService getInstance() {
		if(consumptionService == null) {
			consumptionService = new ConsumptionService();
		}
		return consumptionService;
	}
	
	/**
	 * 没添加具体参与者时，计算平均花费
	 * @param total
	 * @param partNum
	 * @return
	 * @throws Exception
	 */
	public double countAvg(double total, int partNum) throws Exception {
		if(partNum <= 0) {
			throw new Exception("人数不能为0");
		}
		return formatResult(total / partNum);
	}
	

	/**
	 * 计算参与人员的结余情况
	 * @param consumption
	 * @return
	 */
	public List<Map<String, String>> countPartBanlance(Consumption consumption) {
		if(consumption == null) {
			return null;
		}
		List<Map<String, String>> balanceList = new ArrayList<Map<String, String>>();
		double avg = formatResult(consumption.getMoney() / consumption.getParticipantNum());
		for(Map<String, String> map :consumption.getParticipantList()) {
			Map<String, String> banlancemMap = new HashMap<String, String>();
			banlancemMap.put("part_name",  map.get("part_name"));
			double payment = 0;
			if(!map.get("payment").equals("")) {
				payment = Double.parseDouble(map.get("payment"));
			}
			banlancemMap.put("part_id", map.get("part_id"));
			banlancemMap.put("payment", String.valueOf(payment));
			banlancemMap.put("balance", String.valueOf(formatResult(payment - avg))) ;
			balanceList.add(banlancemMap);
		}
		return balanceList;
	}
	
	public List<Map<String, String>> countPartBanlance(Consumption consumption,List<Map<String, String>> payments) {
		if(consumption == null) {
			return null;
		}
		List<Map<String, String>> balanceList = new ArrayList<Map<String, String>>();
		double avg = formatResult(consumption.getMoney() / consumption.getParticipantNum());
		for(Map<String, String> map :payments) {
			Map<String, String> banlancemMap = new HashMap<String, String>();
			banlancemMap.put("part_name",  map.get("part_name"));
			double payment = 0;
			payment = Double.parseDouble(map.get("advance_money"));
			banlancemMap.put("payment", map.get("advance_money"));
			banlancemMap.put("balance", String.valueOf(formatResult(payment - avg))) ;
			balanceList.add(banlancemMap);
		}
		return balanceList;
	}
	
	/**
	 * 校验个人的垫付额是否和消费总额相同
	 * @param consumption
	 * @return
	 */
	public boolean checkPayment(Consumption consumption) {
		String [] paymets = new String[consumption.getParticipantList().size()];
		double total = 0;
		for(int i = 0; i < paymets.length; i++) {
			if(!consumption.getParticipantList().get(i).get("payment").equals("")) {
				total += Double.parseDouble(consumption.getParticipantList().get(i).get("payment"));
			}
		}
		if(total != consumption.getMoney()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据consumption构造payment
	 * @param consumption
	 * @param consumptionId
	 * @return
	 */
	public List<Payment> getPaymentByConsumption(Consumption consumption,long consumptionId) {
		if(consumption == null) {
			return null;
		}
		List<Payment> payments = new ArrayList<Payment>();
		for(Map<String, String> map : consumption.getParticipantList()) {
			Payment payment = new Payment();
			payment.setConsumptionId(consumptionId);
			payment.setPartId(Long.parseLong(map.get("part_id")));
			if(map.get("payment").equals("")) {
				payment.setAdvanceMoney(0);
			}
			else {
				payment.setAdvanceMoney(Double.parseDouble(map.get("payment")));
			}
			payments.add(payment);
		}
		return payments;
	}

	/**
	 * 结算
	 * @param consumptionList
	 * @return
	 */
	public Map<String, SettlementDTO> countSettlement(List<Consumption> consumptionList, PaymentDao paymentDao) {
		double total;
		double avg;
		double advanceMoney;
		double oweMoney;
		String partId;
		List<Map<String, String>> payments;
		Map<String, SettlementDTO> resultMap = new HashMap<String, SettlementDTO>();
		SettlementDTO settlementDTO;
		for(Consumption consumption : consumptionList) {
			total = consumption.getMoney();
			avg = formatResult(total / consumption.getParticipantNum());
			payments = paymentDao.getPayments(consumption.getId());
			
			for(Map<String, String> map : payments) {
				partId = map.get("part_id");
				advanceMoney = Double.parseDouble(map.get("advance_money"));
				oweMoney = formatResult(advanceMoney - avg);
				if(!resultMap.containsKey(partId)) {
					settlementDTO = new SettlementDTO();
					settlementDTO.setTotalMoney(avg);
					settlementDTO.setTotalAdvanceMoney(advanceMoney);
					settlementDTO.setTotalOweMoney(oweMoney);
					resultMap.put(partId, settlementDTO);
				}
				else {
					settlementDTO = resultMap.get(partId);
					settlementDTO.setTotalMoney(formatResult(settlementDTO.getTotalMoney()+avg));
					settlementDTO.setTotalAdvanceMoney(formatResult(settlementDTO.getTotalAdvanceMoney()+advanceMoney));
					settlementDTO.setTotalOweMoney(formatResult(settlementDTO.getTotalOweMoney()+oweMoney));
				}
			}
		}
		return resultMap;
	}
	/**
	 * 格式化计算结果
	 * @param result
	 * @return
	 */
	private double formatResult(double result) {
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.parseDouble(df.format(result));
	}
}
