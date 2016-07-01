package com.bishe.aapay.activity;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.dto.PersonalBudget;
import com.bishe.aapay.util.AApayDateUtil;
import com.bishe.aapay.util.AApayUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonalBillActivity extends Activity {

	private ActionBar actionBar;
	private Button btnPersonalRecord;
	private ImageView imgBudgetFlag;
	private TextView viewPersonCategory;
	private TextView viewPersonConsumeTime;
	private TextView viewPersonConsumeMoney;
	
	private TextView viewPersonTodayTime;
	private TextView viewPersonTodayMoneyIncome;
	private TextView viewPersonTodayMoneyExpand;
	
	private TextView viewPersonWeekTime;
	private TextView viewPersonWeekMoneyIncome;
	private TextView viewPersonWeekMoneyExpand;
	
	private TextView viewPersonMonth;
	private TextView viewPersonMonthMoneyIncome;
	private TextView viewPersonMonthMoneyExpand;
	
	private TextView viewMonth;
	private TextView viewMonthMoneyIncome;
	private TextView viewMonthMoneyExpand;
	
	private LinearLayout layoutRecentRecord;
	private LinearLayout layoutToday;
	private LinearLayout layoutWeek;
	private LinearLayout layoutMonth;
	
	private AApayDBHelper dbHelper;
	private PersonalBudgetDAO personalBudgetDAO;
	
	private PersonalBudget personalBudget;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.personal_bill_activity);
		setContentView(R.layout.activity_personal_bill);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		dbHelper = new AApayDBHelper(this);
		personalBudgetDAO = new PersonalBudgetDAO(dbHelper);
		
		initView();
		
		btnPersonalRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalBillActivity.this,PersonBudgetAddActivity.class);
				startActivity(intent);
				
			}
		});
		
		layoutRecentRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalBillActivity.this,BudgetListActivity.class);
				startActivity(intent);
			}
		});
		
		layoutToday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalBillActivity.this,BudgetTimeListActivity.class);
				intent.putExtra("timeFlag", 1);
				startActivity(intent);
			}
		});
		
		layoutWeek.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalBillActivity.this,BudgetTimeListActivity.class);
				intent.putExtra("timeFlag", 2);
				startActivity(intent);
			}
		});
		
		layoutMonth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalBillActivity.this,BudgetTimeListActivity.class);
				intent.putExtra("timeFlag", 3);
				startActivity(intent);
			}
		});
	}
	
	
	/**
	 * 初始化程序界面
	 */
	private void initView() {
		btnPersonalRecord = (Button) findViewById(R.id.person_btn_record);
		imgBudgetFlag = (ImageView) findViewById(R.id.person_img_budget);
		viewPersonCategory = (TextView) findViewById(R.id.person_view_category);
		viewPersonConsumeTime = (TextView) findViewById(R.id.person_view_consume_time);
		viewPersonConsumeMoney = (TextView) findViewById(R.id.person_view_consume_money);
		
		viewPersonTodayTime = (TextView) findViewById(R.id.person_view_today_time);
		viewPersonTodayMoneyIncome = (TextView) findViewById(R.id.person_view_today_money_income);
		viewPersonTodayMoneyExpand = (TextView) findViewById(R.id.person_view_today_money_expend);

		viewPersonWeekTime = (TextView) findViewById(R.id.person_view_week_time);
		viewPersonWeekMoneyExpand = (TextView) findViewById(R.id.person_view_week_money_expend);
		viewPersonWeekMoneyIncome = (TextView) findViewById(R.id.person_view_week_money_income);
		
		viewPersonMonth = (TextView)findViewById(R.id.person_view_month_time);
		viewPersonMonthMoneyExpand = (TextView) findViewById(R.id.person_view_month_money_expend);
		viewPersonMonthMoneyIncome = (TextView) findViewById(R.id.person_view_month_money_income);
		
		viewMonth = (TextView) findViewById(R.id.person_view_month);
		viewMonthMoneyExpand = (TextView) findViewById(R.id.person_view_month_expand);
		viewMonthMoneyIncome = (TextView) findViewById(R.id.person_view_month_income);
		
		layoutRecentRecord = (LinearLayout) findViewById(R.id.person_layout_recent_record);
		layoutToday = (LinearLayout) findViewById(R.id.budget_layout_today);
		layoutWeek = (LinearLayout) findViewById(R.id.budget_layout_week);
		layoutMonth = (LinearLayout) findViewById(R.id.budget_layout_month);
		initData();
	}
	/**
	 * 初始化界面效果
	 * 2----支出---蓝色
	 * 3----收入---红色
	 */
	private void initData() {
		personalBudget = personalBudgetDAO.getRecentPersonalBudget();
		if(personalBudget == null)
			return;
		
		viewPersonCategory.setText(personalBudget.getBudgetCategory());
		viewPersonConsumeMoney.setText(String.valueOf(personalBudget.getBudgetMoney())+"￥");
		if(personalBudget.getBudgetType() == 2) {
			imgBudgetFlag.setBackgroundResource(R.drawable.budget_expand);
			viewPersonConsumeTime.setText(personalBudget.getBudgetTime()+"【支出】");
			viewPersonConsumeMoney.setTextColor(Color.BLUE);
		}
		else {
			imgBudgetFlag.setBackgroundResource(R.drawable.budget_income);
			viewPersonConsumeTime.setText(personalBudget.getBudgetTime()+"【收入】");
			viewPersonConsumeMoney.setTextColor(Color.RED);			
		}
		
		viewPersonTodayTime.setText(AApayDateUtil.getCurrentDate("yyyy年MM月dd日"));
		double todayTotalIncome = personalBudgetDAO.getTotalMoneyByTimeInterval(AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getCurrentDate()),
				AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getCurrentDate()), 3);
		viewPersonTodayMoneyIncome.setText(String.valueOf(todayTotalIncome));
		double todayTotalExpand = personalBudgetDAO.getTotalMoneyByTimeInterval(AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getCurrentDate()),
				AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getCurrentDate()), 2);
		viewPersonTodayMoneyExpand.setText(String.valueOf(todayTotalExpand));
		
		viewPersonWeekTime.setText(AApayDateUtil.getWeekInterval());
		double weekTotalIncome = personalBudgetDAO.getTotalMoneyByTimeInterval(AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayDate()),
				AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByMonday(6)), 3);
		viewPersonWeekMoneyIncome.setText(String.valueOf(weekTotalIncome));
		double weekTotalExpand = personalBudgetDAO.getTotalMoneyByTimeInterval(AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getMondayDate()),
				AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getDateByMonday(6)), 2);
		viewPersonWeekMoneyExpand.setText(String.valueOf(weekTotalExpand));
		
		viewPersonMonth.setText(AApayDateUtil.getCurrentDate("yyyy年MM月"));
		viewMonth.setText(AApayDateUtil.getCurrentDate("yyyy年MM月"));
		double monthTotalIncome = personalBudgetDAO.getTotalMoneyByTimeInterval(AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getBeginTimeOfCurrentMonth()),
				AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getEndTimeOfCurrenntMonth()), 3);
		viewPersonMonthMoneyIncome.setText(String.valueOf(monthTotalIncome));
		viewMonthMoneyIncome.setText(String.valueOf(monthTotalIncome));
		double monthTotalExpand = personalBudgetDAO.getTotalMoneyByTimeInterval(AApayDateUtil.getBeginTimeOfDate(AApayDateUtil.getBeginTimeOfCurrentMonth()),
				AApayDateUtil.getEndTimeOfDate(AApayDateUtil.getEndTimeOfCurrenntMonth()), 2);
		viewPersonMonthMoneyExpand.setText(String.valueOf(monthTotalExpand));
		viewMonthMoneyExpand.setText(String.valueOf(monthTotalExpand));
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			Intent intent = new Intent(PersonalBillActivity.this,ConsumeActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
