package com.bishe.aapay.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dto.SettlementDTO;
import com.bishe.aapay.service.ConsumptionService;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LvSettleResultAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ConsumptionService consumptionService;
	private List<Long> selectedConsumptionIds;
	private PaymentDao paymentDao;
	private ParticipantDao participantDao;
	private ConsumptionDao consumptionDao;
	Map<String, SettlementDTO> settlementMap;
	public LvSettleResultAdapter(Context context,ArrayList<Long> ids, PaymentDao paymentDao, 
			ParticipantDao participantDao,ConsumptionDao consumptionDao) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.selectedConsumptionIds = ids;
		this.participantDao = participantDao;
		this.paymentDao = paymentDao;
		this.consumptionDao = consumptionDao;
		consumptionService = ConsumptionService.getInstance();
		this.settlementMap = consumptionService.countSettlement(consumptionDao.getConsumptionByIds(ids), paymentDao);
	}
	
	@Override
	public int getCount() {
		return settlementMap.size();
	}

	@Override
	public Object getItem(int position) {
		int i = 0;
		for(String key : settlementMap.keySet()) {
			if(i == position) {
				return settlementMap.get(key);
			}
			i++;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		int i = 0;
		for(String key : settlementMap.keySet()) {
			if(i == position) {
				return Long.parseLong(key);
			}
			i++;
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.settle_result, null);
			holder.viewName = (TextView) convertView.findViewById(R.id.settle_item_result_name);
			holder.viewMoney = (TextView) convertView.findViewById(R.id.settle_item_result_money);
			holder.viewPay = (TextView) convertView.findViewById(R.id.settle_item_result_pay);
			holder.viewOwe = (TextView) convertView.findViewById(R.id.settle_item_result_owe);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		int index = 0;
		SettlementDTO settlementDTO;
		for(String key : settlementMap.keySet()) {
			if(index == position) {
				settlementDTO = settlementMap.get(key);
				holder.viewName.setText(participantDao.getPartName(Long.parseLong(key)));
				holder.viewMoney.setText("œ˚∑—"+String.valueOf(settlementDTO.getTotalMoney()));
				holder.viewPay.setText("µÊ∏∂"+String.valueOf(settlementDTO.getTotalAdvanceMoney()));
				holder.viewOwe.setText("Ω·”‡"+String.valueOf(settlementDTO.getTotalOweMoney()));
			}
			index++;
		}
		return convertView;
	}

	public void setSelectedConsumptionId(List<Long> ids) {
		this.selectedConsumptionIds = ids;
		this.settlementMap = this.consumptionService.countSettlement(consumptionDao.getConsumptionByIds(ids), paymentDao);
	}

	public class ViewHolder {
		public TextView viewName;
		public TextView viewMoney;
		public TextView viewPay;
		public TextView viewOwe;
	}
}
