package com.bishe.aapay.view;

import java.util.Date;
import java.util.List;

import com.bishe.aapay.activity.ConsumeActivity.TimeSelecror;
import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.util.AApayDateUtil;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LvConsumptionListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private ConsumptionDao consumptionDao;
	private long teamId;
	private List<Consumption> consumptionList;
	private TimeSelecror timeSelector;
	private String startTime;
	private String endTime;
	private int interval = 0;
	public LvConsumptionListAdapter(Context context,ConsumptionDao consumptionDao,long teamId,TimeSelecror timeSelector,int interval) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.consumptionDao = consumptionDao;
		this.teamId = teamId;
		this.timeSelector = timeSelector;
		this.interval = interval;
		AApayDateUtil.setGivenDate(new Date());
		switch (timeSelector) {
		case ALL:
			this.consumptionList = consumptionDao.getConsumptionsByTeamId(teamId);
			break;
		case TODAY:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			this.consumptionList = consumptionDao.getConsumptions(teamId,startTime,endTime);
			break;
		case WEEK:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getSundayByInterval(interval));
			this.consumptionList = consumptionDao.getConsumptions(teamId,startTime,endTime);
			break;
		case MONTH:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMonthBeginByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getMonthEndByInterval(interval));
			this.consumptionList = consumptionDao.getConsumptions(teamId,startTime,endTime);
			break;
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
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.consumption_list, null);
			holder.viewName = (TextView) convertView.findViewById(R.id.list_item_name);
			holder.viewTime = (TextView) convertView.findViewById(R.id.list_consume_time);
			holder.viewPeopleNum = (TextView) convertView.findViewById(R.id.list_peple_num);
			holder.viewMoney = (TextView) convertView.findViewById(R.id.list_consumption_total);
			holder.viewPayoff = (TextView) convertView.findViewById(R.id.list_consumption_pay_off);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.viewName.setText(consumptionList.get(position).getName());
		holder.viewTime.setText(consumptionList.get(position).getTime());
		holder.viewPeopleNum.setText(String.valueOf(consumptionList.get(position).getParticipantNum()));
		holder.viewMoney.setText(String.valueOf(consumptionList.get(position).getMoney()));
		holder.viewPayoff.setText(consumptionList.get(position).getPayOFF());
		return convertView;
	}

	public void setTeamId(long teamId,TimeSelecror timeSelector,int interval) {
		this.teamId = teamId;
		this.timeSelector = timeSelector;
		this.interval = interval;
		AApayDateUtil.setGivenDate(new Date());
		switch (timeSelector) {
		case ALL:
			this.consumptionList = consumptionDao.getConsumptionsByTeamId(teamId);
			break;
		case TODAY:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			this.consumptionList = consumptionDao.getConsumptions(teamId,startTime,endTime);
			break;
		case WEEK:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getSundayByInterval(interval));
			this.consumptionList = consumptionDao.getConsumptions(teamId,startTime,endTime);
			break;
		case MONTH:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMonthBeginByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getMonthEndByInterval(interval));
			this.consumptionList = consumptionDao.getConsumptions(teamId,startTime,endTime);
			break;
		}
	}

	public class ViewHolder {
		public TextView viewName;
		public TextView viewTime;
		public TextView viewPeopleNum;
		public TextView viewMoney;
		public TextView viewPayoff;
	}
}
