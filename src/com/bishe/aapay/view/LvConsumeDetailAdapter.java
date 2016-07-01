package com.bishe.aapay.view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.ConsumeSettlementActivity;
import com.bishe.aapay.activity.R;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LvConsumeDetailAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Map<String, String>> consumptionList;
	public LvConsumeDetailAdapter(Context context, List<Map<String, String>> consumptionList) {
		this.context = context;
		this.consumptionList = consumptionList;
		this.inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.consume_detail_item, null);
			viewHolder.viewName = (TextView) convertView.findViewById(R.id.settle_detail_item_name);
			viewHolder.viewTime = (TextView) convertView.findViewById(R.id.settle_detail_item_time);
			viewHolder.viewTotal = (TextView) convertView.findViewById(R.id.settle_detail_item_money);
			viewHolder.viewNum = (TextView) convertView.findViewById(R.id.settle_detail_item_part_num);
			viewHolder.viewPay = (TextView) convertView.findViewById(R.id.settle_detail_item_pay);
			viewHolder.viewAvg = (TextView) convertView.findViewById(R.id.settle_detail_item_avg);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.viewName.setText(consumptionList.get(position).get("consumption_name"));
		viewHolder.viewTime.setText(consumptionList.get(position).get("consumption_time"));
		viewHolder.viewTotal.setText(consumptionList.get(position).get("consumption_money"));
		viewHolder.viewNum.setText(consumptionList.get(position).get("part_num"));
		viewHolder.viewPay.setText(consumptionList.get(position).get("advance_money"));
		viewHolder.viewAvg.setText(formatResult(Double.parseDouble(consumptionList.get(position).get("consumption_money"))/
				Integer.parseInt(consumptionList.get(position).get("part_num"))));
		return convertView;
	}

	public class ViewHolder {
		public TextView viewTime;
		public TextView viewName;
		public TextView viewTotal;
		public TextView viewNum;
		public TextView viewAvg;
		public TextView viewPay;
	}
	/**
	 * 格式化计算结果
	 * @param result
	 * @return
	 */
	private String formatResult(double result) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(result);
	}
}
