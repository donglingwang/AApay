package com.bishe.aapay.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.TeamDao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class LvTeamAdapter extends BaseAdapter {

	private Context context;
	private static List<Map<String, String>> teamLists;
	private LayoutInflater inflater = null;
	private static Map<Integer, Boolean> isSelected;
	private static ArrayList<Integer> selectedPositons;
	private TeamDao teamDao;
	public LvTeamAdapter(Context  context,TeamDao teamDao) {
		this.context = context;
		this.teamDao = teamDao;
		teamLists = teamDao.getTeamList();
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		selectedPositons = new ArrayList<Integer>();
		initData();
	}
	
	private void initData() {
		for(int i = 0; i < teamLists.size(); i++) {
			getIsSelected().put(i, false);
		}
	}
	@Override
	public int getCount() {
		return teamLists.size();
	}

	@Override
	public Object getItem(int position) {
		return teamLists.get(position);
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
			convertView = inflater.inflate(R.layout.team_lv_item, null);
			holder.tView = (TextView) convertView.findViewById(R.id.item_team_tv);
			holder.cBox = (CheckBox) convertView.findViewById(R.id.item_team_cb);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tView.setText(teamLists.get(position).get("team_name"));
		holder.cBox.setChecked(getIsSelected().get(position));
		return convertView;
	}

	public static class ViewHolder {
		public  TextView tView;
		public  CheckBox cBox;
	}
	public static Map<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		LvTeamAdapter.isSelected = isSelected;
	}

	/**
	 * 重置团队数据
	 */
	public void resetTeamData() {
		this.teamLists = teamDao.getTeamList();
		selectedPositons.clear();
		isSelected.clear();
		initData();
	}
	/**
	 * 获取选择的TeamId
	 * @return
	 */
	public static ArrayList<Integer> getSelectedPositons() {
		selectedPositons.clear();
		for(Integer position : isSelected.keySet()) {
			if(isSelected.get(position)) {
				selectedPositons.add(Integer.parseInt(teamLists.get(position).get("team_id")));
			}
		}
		return selectedPositons;
	}
}
