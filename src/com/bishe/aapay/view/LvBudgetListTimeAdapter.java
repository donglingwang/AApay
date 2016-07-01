package com.bishe.aapay.view;

import java.util.Date;
import java.util.List;

import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.dto.PersonalBudget;
import com.bishe.aapay.util.AApayDateUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LvBudgetListTimeAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private PersonalBudgetDAO personalBudgetDAO;
	private int flag;//1日，2周，3月
	private List<PersonalBudget> budgetList;
	private String startTime;
	private String endTime;
	private int interval;
	public LvBudgetListTimeAdapter(Context context,PersonalBudgetDAO personalBudgetDAO,int flag,int interval) {
		this.context = context;
		this.personalBudgetDAO = personalBudgetDAO;
		this.flag = flag;
		this.interval = interval;
		this.inflater = LayoutInflater.from(context);
		AApayDateUtil.setGivenDate(new Date());
		switch (flag) {
		case 1:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			break;
		case 2:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getSundayByInterval(interval));
			break;
		case 3:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMonthBeginByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getMonthEndByInterval(interval));
			break;
		}
		this.budgetList = personalBudgetDAO.getListByTimeInterval(startTime, endTime);
	}
	@Override
	public int getCount() {
		return budgetList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return budgetList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.budget_lv_list_time_item, null);
			holder = new ViewHolder();
			holder.viewDate = (TextView) convertView.findViewById(R.id.budget_item_date);
			holder.viewWeek = (TextView) convertView.findViewById(R.id.budget_item_week);
			holder.imgBudgetType = (ImageView) convertView.findViewById(R.id.budget_item_img);
			holder.viewCategory = (TextView) convertView.findViewById(R.id.budget_item_category);
			holder.viewMoney = (TextView) convertView.findViewById(R.id.budget_item_money);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		PersonalBudget budget = budgetList.get(position);
		holder.viewDate.setText(budget.getBudgetTime().substring(5, 10));
		holder.viewWeek.setText(AApayDateUtil.getWeekByDate(budget.getBudgetTime()));

		if(budget.getBudgetType() == 2) {
			holder.imgBudgetType.setBackgroundResource(R.drawable.budget_expand);
			holder.viewCategory.setText(budget.getBudgetCategory()+"【支出】");
			holder.viewMoney.setTextColor(Color.BLUE);
		}
		else {
			holder.imgBudgetType.setBackgroundResource(R.drawable.budget_income);
			holder.viewCategory.setText(budget.getBudgetCategory()+"【收入】");
			holder.viewMoney.setTextColor(Color.RED);
		}
		holder.viewMoney.setText(String.valueOf(budget.getBudgetMoney()));
		return convertView;
	}

	public class ViewHolder {
		public TextView viewDate;
		public TextView viewWeek;
		public ImageView imgBudgetType;
		public TextView viewCategory;
		public TextView viewMoney;
	}

	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
		AApayDateUtil.setGivenDate(new Date());
		switch (flag) {
		case 1:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			break;
		case 2:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getSundayByInterval(interval));
			break;
		case 3:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMonthBeginByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getMonthEndByInterval(interval));
			break;
		}
		this.budgetList = personalBudgetDAO.getListByTimeInterval(startTime, endTime);
	}
}
