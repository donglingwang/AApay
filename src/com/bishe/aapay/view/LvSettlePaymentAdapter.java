package com.bishe.aapay.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dto.Consumption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class LvSettlePaymentAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ConsumptionDao consumptionDao;
	private static List<Consumption> consumptionList; 
	private long teamId;
	private static Map<Integer, Boolean> isSelected;
	private static ArrayList<Long> selectedPositons;
	public LvSettlePaymentAdapter(Context context,ConsumptionDao consumptionDao,long teamId) {
		this.context = context;
		this.consumptionDao = consumptionDao;
		this.inflater = LayoutInflater.from(context);
		this.teamId = teamId;
		consumptionList = this.consumptionDao.getConsumptionsByTeamId(teamId,"∑Ò");
		isSelected = new HashMap<Integer, Boolean>();
		selectedPositons = new ArrayList<Long>();
		initData();
	}
	
	private void initData() {
		for(int i = 0; i < consumptionList.size(); i++) {
			getIsSelected().put(i, false);
		}
	}
	@Override
	public int getCount() {
		return consumptionList.size();
	}

	@Override
	public Object getItem(int position) {
		return consumptionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.settle_payment, null);
			viewHolder.viewName = (TextView) convertView.findViewById(R.id.settle_item_consume_name);
			viewHolder.viewTime = (TextView) convertView.findViewById(R.id.settle_item_consume_time);
			viewHolder.viewMoney = (TextView) convertView.findViewById(R.id.settle_item_consume_money);
			viewHolder.cBox = (CheckBox) convertView.findViewById(R.id.settle_item_consume_cb);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
			
		}
		viewHolder.viewName.setText(consumptionList.get(position).getName());
		viewHolder.viewTime.setText(consumptionList.get(position).getTime());
		viewHolder.viewMoney.setText(String.valueOf(consumptionList.get(position).getMoney()));
		viewHolder.cBox.setChecked(getIsSelected().get(position));
		
		return convertView;
	}

	public static Map<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(Map<Integer, Boolean> isSelected) {
		LvSettlePaymentAdapter.isSelected = isSelected;
	}

	public static ArrayList<Long> getSelectedPositons() {
		selectedPositons.clear();
		if(!isSelected.isEmpty()) {
			for(Integer position : isSelected.keySet()) {
				if(isSelected.get(position)) {
					selectedPositons.add(consumptionList.get(position).getId());
				}
			}
		}
		return selectedPositons;
	}

	public static void setSelectedPositons(ArrayList<Long> selectedPositons) {
		LvSettlePaymentAdapter.selectedPositons = selectedPositons;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
		resetData();
	}

	/**
	 * ÷ÿ÷√ ˝æ›
	 */
	public  void resetData() {
		consumptionList = this.consumptionDao.getConsumptionsByTeamId(teamId,"∑Ò");
		isSelected.clear();
		initData();
		selectedPositons.clear();
	}
	public static class ViewHolder{
		public  TextView viewName;
		public  TextView viewTime;
		public  TextView viewMoney;
		public  CheckBox cBox;
	}
}
