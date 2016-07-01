package com.bishe.aapay.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.view.LvTeamAdapter;
import com.bishe.aapay.view.LvTeamAdapter.ViewHolder;

import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TeamManageActivity extends Activity {

	private ActionBar actionBar;
	private LinearLayout layoutTeamAdd;
	private LinearLayout layoutTeamEdit;
	
	private ListView listShowTeam;
	private Button btnTeamAdd;
	private Button btnTeamEdit;
	private Button btnTeamFinish;
	private Button btnTeamDelete;
	private Button btnTeamSelectAll;
	
	private String [] teamNames;
	private List<Map<String, String>> teamLists;
	private ArrayAdapter<String> teamNamesAdapter;
	private LvTeamAdapter lvTeamAdapter;
	private AApayDBHelper dbHelper;
	private TeamDao teamDao;
	private ParticipantDao participantDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setTitle(R.string.team_manage_activity);
		setContentView(R.layout.activity_team_manage);
		dbHelper = new AApayDBHelper(this);
		teamDao = new TeamDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		initView();
		
		btnTeamAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TeamManageActivity.this,TeamAddActivity.class);
				startActivity(intent);
			}
		});
		
		btnTeamEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lvTeamAdapter = new LvTeamAdapter(TeamManageActivity.this,teamDao);
				listShowTeam.setAdapter(lvTeamAdapter);
				layoutTeamAdd.setVisibility(View.GONE);
				layoutTeamEdit.setVisibility(View.VISIBLE);
			}
		});
		/*
		 * 点击全选按钮
		 */
		btnTeamSelectAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for(int i = 0; i < teamNames.length; i++) {
					LvTeamAdapter.getIsSelected().put(i, true);
					lvTeamAdapter.notifyDataSetChanged();
				}
			}
		});
		/*
		 * 点击完成按钮
		 */
		btnTeamFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initData();
				teamNamesAdapter.notifyDataSetChanged();
				listShowTeam.setAdapter(teamNamesAdapter);
				layoutTeamAdd.setVisibility(View.VISIBLE);
				layoutTeamEdit.setVisibility(View.GONE);
			}
		});
		/*
		 * 点击删除按钮
		 */
		btnTeamDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				teamDao.deleteByIds(LvTeamAdapter.getSelectedPositons());
				participantDao.deleteByTeamIds(LvTeamAdapter.getSelectedPositons());
				lvTeamAdapter.resetTeamData();
				lvTeamAdapter.notifyDataSetChanged();
			}
		});
		
		listShowTeam.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				if(arg0.getAdapter() instanceof LvTeamAdapter) {
					LvTeamAdapter.ViewHolder holder = (ViewHolder) view.getTag();
					holder.cBox.toggle();
					LvTeamAdapter.getIsSelected().put(position, holder.cBox.isChecked());
				}
				else {
					Intent intent = new Intent(TeamManageActivity.this,ParticiapntManageActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("teamName",teamNames[position]);
					bundle.putString("teamId", teamLists.get(position).get("team_id"));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}
	
	/**
	 * 初始化团队数据
	 */
	private void initData() {
		teamLists = teamDao.getTeamList();
		teamNames = new String[teamLists.size()] ;
		for(int i = 0; i < teamLists.size(); i++) {
			teamNames[i] = teamLists.get(i).get("team_name");
		}
	}
	/**
	 *  初始化界面
	 */
	private void initView() {
		initData();
		layoutTeamAdd = (LinearLayout) findViewById(R.id.team_layout_add);
		layoutTeamEdit = (LinearLayout) findViewById(R.id.team_layout_edit);
		listShowTeam = (ListView) findViewById(R.id.team_list_show);
		teamNamesAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,teamNames);
		listShowTeam.setAdapter(teamNamesAdapter);
				
		btnTeamAdd = (Button) findViewById(R.id.team_btn_add);
		btnTeamEdit = (Button) findViewById(R.id.team_btn_edit);
		btnTeamSelectAll = (Button) findViewById(R.id.team_btn_select_all);
		btnTeamDelete = (Button) findViewById(R.id.team_btn_delete);
		btnTeamFinish = (Button) findViewById(R.id.team_btn_finish);
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
		if(dbHelper != null)
			dbHelper.close();
	}
}
