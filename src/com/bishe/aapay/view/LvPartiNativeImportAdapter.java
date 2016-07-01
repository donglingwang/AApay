package com.bishe.aapay.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.ConsumeActivity;
import com.bishe.aapay.activity.R;
import com.bishe.aapay.activity.TeamAddActivity.ImportFlag;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.service.ContactsService;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class LvPartiNativeImportAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ParticipantDao participantDao;
	private List<Map<String, String>> partiLists;
	private static ArrayList<Map<String, String>> selectedPositons;
	private static Map<Integer, Boolean> isSelected;
	private ImportFlag importFlag;
	public LvPartiNativeImportAdapter(Context context, ParticipantDao participantDao,ImportFlag importFlag) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.participantDao = participantDao;
		if(importFlag == ImportFlag.NATIVE) {
			partiLists = participantDao.getParticipantList();
		}
		else {
			partiLists = ContactsService.getInstance(context.getContentResolver()).getNameAndPhone();
		}
		selectedPositons = new ArrayList<Map<String, String>>();
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}
	
	private void initData() {
		for(int i = 0; i < partiLists.size(); i++) {
			isSelected.put(i, false);
		}
	}
	@Override
	public int getCount() {
		return partiLists.size();
	}

	@Override
	public Object getItem(int position) {
		return partiLists.get(position);
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
			convertView = inflater.inflate(R.layout.participant_lv_import_item, null);
			holder.viewName = (TextView) convertView.findViewById(R.id.participant_item_name);
			holder.viewPhone = (TextView) convertView.findViewById(R.id.participant_item_phone);
			holder.cBox = (CheckBox) convertView.findViewById(R.id.participant_item_cb);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.viewName.setText(partiLists.get(position).get("part_name"));
		holder.viewPhone.setText(partiLists.get(position).get("phone"));
		holder.cBox.setChecked(getIsSelected().get(position));
		return convertView;
	}
	
	public  ArrayList<Map<String, String>> getSelectedPositons() {
		selectedPositons.clear();
		for(Integer position : isSelected.keySet()) {
			if(isSelected.get(position)) {
				selectedPositons.add(partiLists.get(position));
			}
		}
		return selectedPositons;
	}

	public static Map<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	public class ViewHolder {
		public  TextView viewName;
		public TextView viewPhone;
		public  CheckBox cBox;
	}
}
