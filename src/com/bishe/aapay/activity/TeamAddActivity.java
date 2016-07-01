package com.bishe.aapay.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.ConsumeSeptActivity.AAType;
import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.dto.Participant;
import com.bishe.aapay.dto.Team;
import com.bishe.aapay.util.AApayUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TeamAddActivity extends Activity {

	private Button btnManualAdd;
	private Button btnAddressImport;
	private Button btnLocalImport;
	private Button btnConfirm;
	
	private ActionBar actionBar;
	private EditText txtTeamName;
	private EditText txtTeamNum;
	private LinearLayout layoutParticipant;
	
	private RadioGroup rgHasPart;
	private LinearLayout layoutBtn;
	
	private Team team;
	private List<Participant> participants;
	
	private AApayDBHelper dbHelper;
	private TeamDao teamDao;
	private ParticipantDao participantDao;
	
	public enum ImportFlag {CONTACTS,NATIVE};
	private ImportFlag importFlag = ImportFlag.NATIVE;
	private AAType aaType = AAType.HavaPart;
	/*
	 * 手动添加参与人员Dialog
	 */
	private View formParticipantAdd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.team_add_activity);
		setContentView(R.layout.activity_team_add);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		initView();
		dbHelper = new AApayDBHelper(this);
		teamDao = new TeamDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		
		rgHasPart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.team_radio_has) {
					txtTeamNum.setVisibility(View.GONE);
					layoutBtn.setVisibility(View.VISIBLE);
					aaType = AAType.HavaPart;
				}
				else {
					txtTeamNum.setVisibility(View.VISIBLE);
					layoutBtn.setVisibility(View.GONE);
					aaType = AAType.NoPart;
				}
			}
		});
		/*
		 * 手动添加 
		 */
		btnManualAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createManualAddDialog();
			}
		});
		/*
		 * 通讯录导入
		 * 标记为0
		 */
		btnAddressImport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeamAddActivity.this,ParticipantImportActivity.class);
				intent.putExtra("importFlag", ImportFlag.CONTACTS);
				startActivityForResult(intent, 0);
			}
		});
		/*
		 * 本地导入
		 * 标记为1
		 */
		btnLocalImport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeamAddActivity.this,ParticipantImportActivity.class);
				intent.putExtra("importFlag", ImportFlag.NATIVE);
				startActivityForResult(intent, 1);
			}
		});
		/*
		 * 确定添加
		 */
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				team = new Team();
				if(txtTeamName.getText().toString().equals("")) {
					Toast.makeText(TeamAddActivity.this, "团队名称不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				team.setTeamName(txtTeamName.getText().toString());
				if(aaType == AAType.HavaPart) {
					if(participants.size() == 0) {
						Toast.makeText(TeamAddActivity.this, "请至少添加一个成员!", Toast.LENGTH_SHORT).show();
						return;
					}
					team.setHasPart("是");
					team.setTeamNum(participants.size());
				}
				else {
					if(txtTeamNum.getText().toString().equals("")||Integer.parseInt(txtTeamNum.getText().toString()) < 1) {
						Toast.makeText(TeamAddActivity.this, "请输入团队人数!", Toast.LENGTH_SHORT).show();
						return;
					} 
					team.setHasPart("否");
					team.setTeamNum(Integer.parseInt(txtTeamNum.getText().toString()));
				}
				team.setParticipants(participants);
				team.setFoundDate(AApayUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				
				long teamId = teamDao.insertTeam(team);
				participantDao.insertParticipantList(participants,teamId);
				//TODO 跳转到别的界面
				Intent intent = new Intent(TeamAddActivity.this,ConsumeActivity.class);
				startActivity(intent);
			}
		});
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
	/**
	 * 初始化界面
	 */
	private void initView() {
		
		btnManualAdd = (Button) findViewById(R.id.team_btn_manual_add);
		btnAddressImport = (Button) findViewById(R.id.team_btn_address_import);
		btnLocalImport = (Button) findViewById(R.id.team_btn_local_import);
		btnConfirm = (Button) findViewById(R.id.team_btn_confirm);
		
		txtTeamName = (EditText) findViewById(R.id.team_txt_name);
		txtTeamNum = (EditText) findViewById(R.id.team_txt_num);
		rgHasPart = (RadioGroup) findViewById(R.id.team_rg_haspart);
		layoutBtn = (LinearLayout) findViewById(R.id.team_layout_btn);
		
		layoutParticipant = (LinearLayout) findViewById(R.id.team_layout_participant);
		
		formParticipantAdd = getLayoutInflater().inflate(R.layout.participant_add, null);
		
		participants = new ArrayList<Participant>();
	}
	
	/**
	 * 创建手动添加的对话框
	 */
	private void createManualAddDialog() {
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
				if(participantName.getText().toString().equals("")||phone.getText().toString().equals("")) {
					Toast.makeText(TeamAddActivity.this, "请输入相应的数据", Toast.LENGTH_SHORT).show();
					((ViewGroup)formParticipantAdd.getParent()).removeAllViews();
					return;
				}
				showParticipantList(participantName.getText().toString(), phone.getText().toString());
				//txtTeamNum.setText(String.valueOf(participants.size()));
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
	
	/**
	 * 添加参与人员并在主界面上面显示
	 * @param partName
	 * @param phone
	 */
	private void showParticipantList(String partName,String phone) {
		
		Participant participant = new Participant(partName, phone);
		if(!addParticipantList(participant)) {
			return;
		}
		
		LinearLayout layout = new LinearLayout(TeamAddActivity.this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = 
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.setTag(participant);
		
		TextView viewName = new TextView(TeamAddActivity.this);
		viewName.setText(partName);
		LinearLayout.LayoutParams paramsName = 
				new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,1);
		TextView viewPhone = new TextView(TeamAddActivity.this);
		viewPhone.setText(phone);
		LinearLayout.LayoutParams paramsPhone = 
				new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT,2);
		ImageView imageDel = new ImageView(TeamAddActivity.this);
		imageDel.setBackgroundResource(R.drawable.remove);
		imageDel.setOnClickListener(new ImageClickListener());
		LinearLayout.LayoutParams paramsDel = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		
		layout.addView(viewName,paramsName);
		layout.addView(viewPhone,paramsPhone);
		layout.addView(imageDel,paramsDel);
		
		layoutParticipant.addView(layout,layoutParams);
	}
	
	/**
	 * 给participants里面参加合适的参与者
	 * @param participant
	 * @return
	 */
	private boolean addParticipantList(Participant participant) {
		if(participants.contains(participant)) {
			Toast.makeText(TeamAddActivity.this, "姓名或电话重复！请仔细核对！", Toast.LENGTH_SHORT).show();
			return false;
		}
		participants.add(participant);
		return true;
	}
	/**
	 * 删除添加错误的参与者
	 * @author DongLing
	 *
	 */
	private class ImageClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Participant participant = (Participant)((View)v.getParent()).getTag();
			participants.remove(participant);
			txtTeamNum.setText(String.valueOf(participants.size()));
			((ViewGroup)v.getParent().getParent()).removeView((View)v.getParent());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 *通讯录导入
		 */
		if(requestCode == 0 && resultCode == 1) {
			ArrayList<Map<String, String>> selectedParts = (ArrayList<Map<String, String>>) data.getSerializableExtra("selectedParts");
			for(Map<String,String> map : selectedParts) {
				showParticipantList(map.get("part_name"), map.get("phone"));
			}
		}
		/*
		 *本地导入
		 */
		if(requestCode == 1 && resultCode == 1) {
			ArrayList<Map<String, String>> selectedParts = (ArrayList<Map<String, String>>) data.getSerializableExtra("selectedParts");
			for(Map<String,String> map : selectedParts) {
				showParticipantList(map.get("part_name"), map.get("phone"));
			}
		}
	}
	
	/**
	 * 关闭数据库连接
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}
}
