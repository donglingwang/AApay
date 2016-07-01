package com.bishe.aapay.view;

import java.util.List;

import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.dto.PersonalBudget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LvBudgetListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private PersonalBudgetDAO personalBudgetDAO;
	private List<PersonalBudget> budgetList;
	public LvBudgetListAdapter(Context context,PersonalBudgetDAO personalBudgetDAO) {
		this.context = context;
		this.personalBudgetDAO = personalBudgetDAO;
		this.inflater = LayoutInflater.from(context);
		this.budgetList = personalBudgetDAO.getPersonalBudgetList();
	}
	@Override
	public int getCount() {
		return budgetList.size();
	}

	@Override
	public Object getItem(int position) {
		return budgetList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.budget_lv_list_item, null);
			holder = new ViewHolder();
			holder.imgFlag = (ImageView) convertView.findViewById(R.id.budget_item_img);
			holder.viewCategory = (TextView) convertView.findViewById(R.id.budget_item_category);
			holder.viewTime = (TextView) convertView.findViewById(R.id.budget_item_consume_time);
			holder.viewMoney = (TextView) convertView.findViewById(R.id.budget_item_view_money);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		PersonalBudget budget = budgetList.get(position);
		if (budget.getBudgetType() == 2) {
			holder.imgFlag.setBackgroundResource(R.drawable.budget_expand);
			holder.viewCategory.setText(budget.getBudgetCategory()+"【支出】");
			holder.viewMoney.setTextColor(Color.BLUE);
		}
		else {
			holder.imgFlag.setBackgroundResource(R.drawable.budget_income);
			holder.viewCategory.setText(budget.getBudgetCategory()+"【收入】");
			holder.viewMoney.setTextColor(Color.RED);
		}
		holder.viewTime.setText(budget.getBudgetTime());
		holder.viewMoney.setText(String.valueOf(budget.getBudgetMoney()));
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView imgFlag;
		public TextView viewCategory;
		public TextView viewTime;
		public TextView viewMoney;
	}
}
