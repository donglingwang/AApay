package com.bishe.aapay.activity;

import java.util.Date;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.service.ConsumptionService;
import com.bishe.aapay.util.AApayDateUtil;
import com.bishe.aapay.view.LvBudgetListTimeAdapter;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BudgetTimeListActivity extends Activity {

	private ActionBar actionBar;
	private AApayDBHelper dbHelper;
	private PersonalBudgetDAO personalBudgetDAO;
	

	private TextView viewIncome;
	private TextView viewExpand;
	private TextView viewBalance;
	private ListView lvTimeList;
	private Button btnPre;
	private Button btnNext;
	
	private String startTime;
	private String endTime;
/*	private int dayFlag = 0;
	private int weekFlag = 0;
	private int monthFlag = 0;*/
	
	private int interval = 0;
	private int timeFlag = 1;
	private LvBudgetListTimeAdapter lvBudgetListTimeAdapter;
	private ListView lvListTime;
	private TextView viewTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.budget_time_list_activity);
		setContentView(R.layout.activity_budget_time_list);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		dbHelper = new AApayDBHelper(this);
		personalBudgetDAO = new PersonalBudgetDAO(dbHelper);
		Intent intent = getIntent();
		timeFlag = intent.getIntExtra("timeFlag", 1);
		initView();
		
		/**
		 * 点击上一天，周，月
		 */
		btnPre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				interval--;
				resetValueByTime();
			}
		});
		
		/*
		 * 点击下一天，周，月
		 */
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				interval++;
				resetValueByTime();
			}
		});
	}
	/**
	 * 每次更新页面前端的数据
	 */
	private void resetValueByTime() {
		AApayDateUtil.setGivenDate(new Date());
		switch (timeFlag) {
		case 1:
			viewTime.setText(AApayDateUtil.getDateByInterval(interval));
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByInterval(interval));
			break;
		case 2:
			viewTime.setText(AApayDateUtil.getMondayByInterval((interval))+"~"+AApayDateUtil.getSundayByInterval(interval));
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getSundayByInterval(interval));
			break;
		case 3:
			viewTime.setText(AApayDateUtil.getMonthBeginByInterval((interval))+"~"+AApayDateUtil.getMonthEndByInterval(interval));
			startTime = AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMonthBeginByInterval((interval)));
			endTime = AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getMonthEndByInterval(interval));
			break;
		}
		double expand = personalBudgetDAO.getTotalMoneyByTimeInterval(startTime, endTime, 2);
		double income = personalBudgetDAO.getTotalMoneyByTimeInterval(startTime, endTime, 3);
		
		viewIncome.setText(String.valueOf(income));
		viewExpand.setText(String.valueOf(expand));
		viewBalance.setText(String.valueOf(income - expand));
		lvBudgetListTimeAdapter.setInterval(interval);
		lvBudgetListTimeAdapter.notifyDataSetChanged();
	}
	/**
	 * 初始化界面
	 */
	private void initView() {
		viewIncome = (TextView) findViewById(R.id.budget_time_view_income);
		viewExpand = (TextView) findViewById(R.id.budget_time_view_expand);
		viewBalance = (TextView) findViewById(R.id.budget_time_view_balance);
		viewTime = (TextView) findViewById(R.id.budget_time_view_time);
		
		lvListTime = (ListView) findViewById(R.id.budget_time_lv_list);
		btnPre = (Button) findViewById(R.id.budget_time_btn_pre);
		btnNext = (Button) findViewById(R.id.budget_time_btn_next);
		
		lvBudgetListTimeAdapter = new LvBudgetListTimeAdapter(this, personalBudgetDAO, timeFlag,interval);
		
		lvListTime.setAdapter(lvBudgetListTimeAdapter);
		
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		AApayDateUtil.setGivenDate(new Date());
		switch (timeFlag) {
		case 1:
			btnPre.setText("上一天");
			btnNext.setText("下一天");
			break;
		case 2:
			btnPre.setText("上一周");
			btnNext.setText("下一周");
			break;
		case 3:
			btnPre.setText("上一月");
			btnNext.setText("下一月");
			break;
		}
		resetValueByTime();
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		interval = 0;
		lvBudgetListTimeAdapter.setInterval(interval);
		lvBudgetListTimeAdapter.notifyDataSetChanged();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
		super.onDestroy();
	}
}
