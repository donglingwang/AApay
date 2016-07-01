package com.bishe.aapay.activity;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.dto.Participant;
import com.bishe.aapay.dto.Team;
import com.bishe.aapay.view.LvParticipantAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ParticiapntManageActivity extends Activity {

	private TextView viewTeamName;
	private TextView viewCreateTime;
	private TextView viewPartNum;
	private TextView participantName;
	private EditText txtPartNum;
	private Button btnTeamModify;
	private ListView lvParticipants;
	private LvParticipantAdapter lvParticipantAdapter;
	private Button btnManualAdd;
	private String teamId;
	private String teamName;
	private ActionBar actionBar;
	
	private LinearLayout layoutOperationShow;
	private LinearLayout layoutPartAdd;
	
	private AApayDBHelper dbHelper;
	private TeamDao teamDao;
	private ParticipantDao participantDao;
	
	private View formParticipantAdd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.participant_manage);
		setContentView(R.layout.activity_participant_manage);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		dbHelper = new AApayDBHelper(this);
		teamDao = new TeamDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		initView();
		
		btnTeamModify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(txtPartNum.getText().toString().equals("")) {
					Toast.makeText(ParticiapntManageActivity.this, "请输入参与人员人数", Toast.LENGTH_SHORT).show();
					return;
				}
				teamDao.updateTeamNum(teamId, txtPartNum.getText().toString());
				
			}
		});
		btnManualAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showManualAddDialog();
			}
		});
	}
	
	private void initView() {
		viewTeamName = (TextView) findViewById(R.id.team_view_name);
		viewCreateTime = (TextView) findViewById(R.id.team_view_create_time);
		viewPartNum = (TextView) findViewById(R.id.team_view_part_num);
		txtPartNum = (EditText) findViewById(R.id.team_txt_part_num);
		participantName = (TextView) findViewById(R.id.participant_part_name);
		btnTeamModify = (Button) findViewById(R.id.team_btn_modify);
		
		lvParticipants = (ListView) findViewById(R.id.participant_lv_list);
		btnManualAdd = (Button) findViewById(R.id.part_btn_manual_add);
		
		layoutOperationShow = (LinearLayout) findViewById(R.id.participant_operation_show);
		layoutPartAdd = (LinearLayout) findViewById(R.id.participant_add);
		
		Intent intent = getIntent();
		teamId = intent.getStringExtra("teamId");
		teamName = intent.getStringExtra("teamName");
		viewTeamName.setText(teamName);
		Team team = teamDao.geTeamById(teamId);
		viewCreateTime.setText(team.getFoundDate());
		viewPartNum.setText(String.valueOf(team.getTeamNum()));
		if(participantDao.getParticipantList(Long.parseLong(teamId)).size() == 0) {
			viewPartNum.setVisibility(View.GONE);
			layoutOperationShow.setVisibility(View.GONE);
			layoutPartAdd.setVisibility(View.GONE);
			participantName.setVisibility(View.GONE);
			btnTeamModify.setVisibility(View.VISIBLE);
			txtPartNum.setVisibility(View.VISIBLE);
			txtPartNum.setText(String.valueOf(team.getTeamNum()));
		}
		//String [] participants = participantDao.getParticipantNamesByTeamId(Long.parseLong(teamId));
		/*lvParticipants.setAdapter(new SimpleAdapter(ParticiapntManageActivity.this, 
				participantDao.getParticipantList(Long.parseLong(teamId)), R.layout.participant_lv_item,
				new String[]{"part_name","phone"}, 
				new int[]{R.id.participant_item_name,R.id.participant_item_phone}));*/
		lvParticipantAdapter = 
				new LvParticipantAdapter(ParticiapntManageActivity.this, teamId,participantDao,teamDao,viewPartNum);
		lvParticipants.setAdapter(lvParticipantAdapter);
		formParticipantAdd = this.getLayoutInflater().inflate(R.layout.participant_add, null);
	}

	void showManualAddDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final EditText participantName = (EditText) formParticipantAdd.findViewById(R.id.team_txt_participant_name);
		final	EditText phone = (EditText) formParticipantAdd.findViewById(R.id.team_txt_phone);
		participantName.setText("");
		phone.setText("");
		builder.setTitle(R.string.team_manual_add)
		.setView(formParticipantAdd)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Participant participant = new Participant();
				participant.setName(participantName.getText().toString());
				participant.setPhone(phone.getText().toString());
				participant.setTeamId(Long.parseLong(teamId));
				participantDao.insertPart(participant);
				viewPartNum.setText(String.valueOf(participantDao.getParticipantList(Long.parseLong(teamId)).size()));
				teamDao.updateTeamNum(teamId, String.valueOf(participantDao.getParticipantList(Long.parseLong(teamId)).size()));
				lvParticipantAdapter.setParticipantList();
				lvParticipantAdapter.notifyDataSetChanged();
				((ViewGroup)formParticipantAdd.getParent()).removeAllViews();
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((ViewGroup)formParticipantAdd.getParent()).removeAllViews();
			}
		});
		builder.create().show();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dbHelper != null) {
			dbHelper.close();
		}
	}
}
