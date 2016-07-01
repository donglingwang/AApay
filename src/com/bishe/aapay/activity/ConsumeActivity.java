package com.bishe.aapay.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.util.AApayDateUtil;
import com.bishe.aapay.view.LvConsumptionListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumeActivity extends Activity {

	private Button btnBilling;
	private Button btnTeamAdd;
	private TextView viewQueryTime;
	private TextView viewTotalMoney;
	private TextView viewSettleMoney;
	
	private Button btnPersonalBilling;
	private Button btnSettlement;
	private Button btnBillFlowing;
	private Button btnChart;
	private Button btnSetting;
	
	private Spinner spinnerTeamName;
	private ListView listViewConsumption;
	private AApayDBHelper dbHelper;
	private ConsumptionDao consumptionDao;
	private long selectedTeamId;
	private String selectedTeamName;
	private String [] teamNames;
	private List<Map<String, String>> teamLists;
	private TeamDao teamDao;
	
	public enum TimeSelecror {TODAY,WEEK,MONTH,ALL};
	private TimeSelecror timeSelector = TimeSelecror.ALL;
	
	private String startTime;
	private String endTime;
	private LvConsumptionListAdapter lvConsumptionListAdapter;
	private ArrayAdapter<String> spinnerAdapter;
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consume);
		//setTitle(R.string.consume_activity);
		
		dbHelper = new AApayDBHelper(this);
		teamDao = new TeamDao(dbHelper);
		consumptionDao = new ConsumptionDao(dbHelper);
		preferences = getSharedPreferences("loginFlag", MODE_PRIVATE);
		editor = preferences.edit();
		initView();
		
		getOverflowMenu();
		/*
		 * 打开记账页面
		 */
		btnBilling.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConsumeActivity.this,ConsumeSeptActivity.class);
				startActivity(intent);
			}
		});
		
		/*
		 * 打开添加团队界面
		 */
		btnTeamAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConsumeActivity.this,TeamAddActivity.class);
				startActivity(intent);
			}
		});
		/*
		 * 选择不同的团队查看
		 */
		spinnerTeamName.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectedTeamId = Long.parseLong(teamLists.get(position).get("team_id"));
				selectedTeamName = teamNames[position];
				resetHeadData();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		/*
		 * 查看某次消费明细
		 */
		listViewConsumption.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Consumption consumption = (Consumption) lvConsumptionListAdapter.getItem(position);
				Toast.makeText(ConsumeActivity.this, String.valueOf(consumption.getId()), Toast.LENGTH_SHORT).show();	
				Intent intent = new Intent(ConsumeActivity.this,ConsumptionViewActivity.class);
				intent.putExtra("consumption", consumption);
				intent.putExtra("teamName", selectedTeamName);
				startActivity(intent);
			}
		});
		
		/*
		 * 个人收支统计
		 */
		btnPersonalBilling.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(ConsumeActivity.this,PersonalBillActivity.class);
				startActivity(intent);
			}
		});
		
		/*
		 * 账单结算按钮
		 */
		btnSettlement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConsumeActivity.this,ConsumeSettlementActivity.class);
				intent.putExtra("teamPosition", spinnerTeamName.getSelectedItemPosition());
				startActivity(intent);
			}
		});
		/*
		 * 流水统计按钮
		 */
		btnBillFlowing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent intent = new Intent(ConsumeActivity.this,ConsumptionListActivity.class);
				intent.putExtra("teamPosition", spinnerTeamName.getSelectedItemPosition());
				startActivity(intent);
			}
		});
		/*
		 * 图表统计
		 */
		btnChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConsumeActivity.this,ChartStatisticsActivity.class);
				startActivity(intent);
			}
		});
		/*
		 * 更多按钮
		 */
		btnSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConsumeActivity.this,SettingActivity.class);
				startActivity(intent);
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
		if(teamLists.size() > 1) {
			selectedTeamId = Long.parseLong(teamLists.get(0).get("team_id"));
			selectedTeamName = teamNames[0];
		}
	}
	
	/**
	 * 初始化界面组件
	 */
	private void initView() {
		initData();
		viewQueryTime = (TextView) findViewById(R.id.consume_view_query_time);
		viewTotalMoney = (TextView) findViewById(R.id.consume_view_total_money);
		viewSettleMoney = (TextView) findViewById(R.id.consume_view_settle_money);
		btnBilling = (Button) findViewById(R.id.consume_btn_billing);
		btnTeamAdd = (Button) findViewById(R.id.consume_btn_team_add);
		
		btnPersonalBilling = (Button) findViewById(R.id.personal_btn_billing);
		btnSettlement = (Button) findViewById(R.id.bill_btn_settlement);
		btnBillFlowing = (Button) findViewById(R.id.bill_btn_flowing);
		btnChart = (Button) findViewById(R.id.aapay_btn_chart);
		btnSetting = (Button) findViewById(R.id.aapay_btn_setting);
		
		
		viewQueryTime.setText("全部数据");
		viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(selectedTeamId)));
		viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(selectedTeamId,"是")));
		
		
		spinnerTeamName = (Spinner) findViewById(R.id.consume_spin_team);
		spinnerAdapter = new ArrayAdapter<String>(ConsumeActivity.this, 
				android.R.layout.simple_list_item_1, teamNames);
		spinnerTeamName.setAdapter(spinnerAdapter);
		
		listViewConsumption = (ListView) findViewById(R.id.consume_list_consumption);
		
		lvConsumptionListAdapter = new LvConsumptionListAdapter(this, consumptionDao, selectedTeamId,timeSelector,0);
		listViewConsumption.setAdapter(lvConsumptionListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.consume, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
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
		case R.id.action_settings:
			Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
			break;
		}
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
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(selectedTeamId)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(selectedTeamId,"是")));
			break;
		case TODAY:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getDateByInterval(0));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByInterval(0));
			viewQueryTime.setText(AApayDateUtil.getDateByInterval(0));
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(selectedTeamId,startTime,endTime)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(selectedTeamId,startTime,endTime,"是")));
			break;
		case WEEK:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayByInterval(0));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getSundayByInterval(0));
			viewQueryTime.setText(AApayDateUtil.getWeekInterval());
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(selectedTeamId,startTime,endTime)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(selectedTeamId,startTime,endTime,"是")));
			break;
		case MONTH:
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMonthBeginByInterval(0));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getMonthEndByInterval(0));
			viewQueryTime.setText(AApayDateUtil.getMonthBeginByInterval(0)+"~"+AApayDateUtil.getMonthEndByInterval(0));
			viewTotalMoney.setText(String.valueOf(consumptionDao.getTotalMoney(selectedTeamId,startTime,endTime)));
			viewSettleMoney.setText(String.valueOf(consumptionDao.getSettleMoney(selectedTeamId,startTime,endTime,"是")));
			break;
		}
		lvConsumptionListAdapter.setTeamId(selectedTeamId,timeSelector,0);
		lvConsumptionListAdapter.notifyDataSetChanged();
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
	protected void onResume() {
		super.onResume();
		initData();
		spinnerAdapter = new ArrayAdapter<String>(ConsumeActivity.this, 
				android.R.layout.simple_list_item_1, teamNames);
		spinnerTeamName.setAdapter(spinnerAdapter);
		resetHeadData();
		//lvConsumptionListAdapter.setTeamId(selectedTeamId,timeSelector,0);
		//lvConsumptionListAdapter.notifyDataSetChanged();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		editor.putString("islogin", "no");
		editor.commit();
		if(dbHelper != null ) {
			dbHelper.close();
		}
	}
}
