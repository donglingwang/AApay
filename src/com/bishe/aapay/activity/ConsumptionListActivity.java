package com.bishe.aapay.activity;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.ConsumeActivity.TimeSelecror;
import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.util.AApayDateUtil;
import com.bishe.aapay.view.LvConsumptionListAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 查看AA消费流水信息
 * 全部数据
 * 天，
 * 周，
 * 月
 * @author DongLing
 *
 */
public class ConsumptionListActivity extends Activity {

	private TextView viewQueryTime;
	private TextView viewTotalMoney;
	private TextView viewSettleMoney;
	private ListView lvConsumptionList;
	private Spinner spinnerTeamNames;
	private LinearLayout layoutBtn;
	private Button btnPre;
	private Button btnNext;
	private LvConsumptionListAdapter lvConsumptionListAdapter;
	private ActionBar actionBar;
	private long teamId;
	private int teamPosition;
	private AApayDBHelper dbHelper;
	private ConsumptionDao consumptionDao;
	private TeamDao teamDao;
	private String [] teamNames;
	private List<Map<String, String>> teamLists;
	private TimeSelecror timeSelector = TimeSelecror.ALL;
	private int interval = 0;
	private String startTime;
	private String endTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consume_list);
		setTitle(R.string.consume_list_activity);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		teamPosition = intent.getIntExtra("teamPosition", 1);
		dbHelper = new AApayDBHelper(this);
		consumptionDao = new ConsumptionDao(dbHelper);
		teamDao = new TeamDao(dbHelper);
		
		getOverflowMenu();
		initView();
		
		spinnerTeamNames.setSelection(teamPosition);
		spinnerTeamNames.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				teamId = Long.parseLong(teamLists.get(position).get("team_id"));
				resetHeadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		btnPre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				interval--;
				resetHeadData();
			}
		});
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				interval++;
				resetHeadData();
			}
		});
	}
	
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		teamLists = teamDao.getTeamList();
		teamNames = new String[teamLists.size()] ;
		for(int i = 0; i < teamLists.size(); i++) {
			teamNames[i] = teamLists.get(i).get("team_name");
		}
	}
	/**
	 * 初始化界面
	 */
	private void initView() {
		initData();
		viewQueryTime = (TextView) findViewById(R.id.consume_view_query_time);
		viewTotalMoney = (TextView) findViewById(R.id.consume_view_total_money);
		viewSettleMoney = (TextView) findViewById(R.id.consume_view_settle_money);
		lvConsumptionList = (ListView) findViewById(R.id.consume_lv_list);
		spinnerTeamNames = (Spinner) findViewById(R.id.consume_spin_team);
		layoutBtn = (LinearLayout) findViewById(R.id.consume_layout_btn);
		
		btnPre = (Button) findViewById(R.id.consume_btn_pre);
		btnNext = (Button) findViewById(R.id.consume_btn_next);
		
		spinnerTeamNames.setAdapter(new ArrayAdapter<String>(ConsumptionListActivity.this, 
				android.R.layout.simple_list_item_1, teamNames));
		lvConsumptionListAdapter = new LvConsumptionListAdapter(this, consumptionDao, teamId,timeSelector,interval);
		lvConsumptionList.setAdapter(lvConsumptionListAdapter);
		
		setButtonTextByTime();
	}
	
	private void setButtonTextByTime() {
		switch (timeSelector) {
		case ALL:
			btnPre.setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
			layoutBtn.setVisibility(View.GONE);
			break;
		case TODAY:
			layoutBtn.setVisibility(View.VISIBLE);
			btnPre.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);
			btnPre.setText("上一天");
			btnNext.setText("下一天");
			break;
		case WEEK:
			btnPre.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);
			layoutBtn.setVisibility(View.VISIBLE);
			btnPre.setText("上一周");
			btnNext.setText("下一周");
			break;
		case MONTH:
			btnPre.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);
			layoutBtn.setVisibility(View.VISIBLE);
			btnPre.setText("上一月");
			btnNext.setText("下一月");
			break;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.consumption_list, menu);
		return super.onCreateOptionsMenu(menu);
	}
   /**
    * 用反射的方法显示actionbar的overflow按钮
    */
	private void getOverflowMenu() {
	      try {
	         ViewConfiguration config = ViewConfiguration.get(this);
	         Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	         if(menuKeyField != null) {
	             menuKeyField.setAccessible(true);
	             menuKeyField.setBoolean(config, false);
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	 }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			Intent intent = new Intent(ConsumptionListActivity.this,ConsumeActivity.class);
			startActivity(intent);
			break;
		case R.id.action_all:
			timeSelector = TimeSelecror.ALL;
			break;
		case R.id.action_today:
			timeSelector = TimeSelecror.TODAY;
			break;
		case R.id.action_week:
			timeSelector = TimeSelecror.WEEK;
			break;
		case R.id.action_month:
			timeSelector = TimeSelecror.MONTH;
			break;
		}
		setButtonTextByTime();
		resetHeadData();
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 根据选择的查看种类
	 * 重新设置界面前端view中的数据
	 */
	private void resetHeadData() {
		AApayDateUtil.setGivenDate(new Date());
		switch (timeSelector) {
		case ALL:
			viewQueryTime.setText("全部数据");
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(teamId)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(teamId,"是")));
			break;
		case TODAY:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			viewQueryTime.setText(AApayDateUtil.getDateByInterval(interval));
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(teamId,startTime,endTime)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(teamId,startTime,endTime,"是")));
			break;
		case WEEK:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getSundayByInterval(interval));
			viewQueryTime.setText(AApayDateUtil.getMondayByInterval(interval)+"~"+AApayDateUtil.getSundayByInterval(interval));
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(teamId,startTime,endTime)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(teamId,startTime,endTime,"是")));
			break;
		case MONTH:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMonthBeginByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getMonthEndByInterval(interval));
			viewQueryTime.setText(AApayDateUtil.getMonthBeginByInterval(interval)+"~"+AApayDateUtil.getMonthEndByInterval(interval));
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(teamId,startTime,endTime)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(teamId,startTime,endTime,"是")));
			break;
		}
		lvConsumptionListAdapter.setTeamId(teamId,timeSelector,interval);
		lvConsumptionListAdapter.notifyDataSetChanged();
	}
	@Override
	protected void onResume() {
		Intent intent = getIntent();
		teamPosition = intent.getIntExtra("teamPosition", 1);
		Toast.makeText(this, teamPosition+"", Toast.LENGTH_SHORT).show();
		spinnerTeamNames.setSelection(teamPosition);
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dbHelper != null) {
			dbHelper = null;
		}
	}
}
