package com.bishe.aapay.view;

import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.R;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.TeamDao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LvParticipantAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private List<Map<String, String>> participantList;
	private ParticipantDao participantDao;
	private TeamDao teamDao;
	private TextView viewPartNum;
	private long teamId;
	public LvParticipantAdapter(Context context,String teamId,ParticipantDao participantDao,TeamDao teamDao,TextView viewPartNum) {
		this.context = context;
		this.teamId = Long.parseLong(teamId);
		this.participantList = participantDao.getParticipantList(this.teamId);
		this.inflater = LayoutInflater.from(context);
		this.viewPartNum = viewPartNum;
		this.participantDao = participantDao;
		this.teamDao = teamDao;
	}
	@Override
	public int getCount() {
		return participantList.size();
	}

	@Override
	public Object getItem(int position) {
		return participantList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.participant_lv_item, null);
			holder.viewName = (TextView) convertView.findViewById(R.id.participant_item_name);
			holder.viewPhone = (TextView) convertView.findViewById(R.id.participant_item_phone);
			holder.imageRemove = (ImageView) convertView.findViewById(R.id.participant_item_remove);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.viewName.setText(participantList.get(position).get("part_name"));
		holder.viewPhone.setText(participantList.get(position).get("phone"));
		holder.imageRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String partId = participantList.get(position).get("part_id");
				participantDao.deleteById(partId);
				participantList.remove(position);
				viewPartNum.setText(String.valueOf(participantList.size()));
				teamDao.updateTeamNum(String.valueOf(teamId), String.valueOf(participantList.size()));
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	public void setParticipantList() {
		this.participantList = participantDao.getParticipantList(teamId);
	}
	public class ViewHolder {
		public TextView viewName;
		public TextView viewPhone;
		public ImageView imageRemove;
	}
}
