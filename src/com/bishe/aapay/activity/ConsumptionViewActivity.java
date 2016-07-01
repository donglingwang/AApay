package com.bishe.aapay.activity;

import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.ConsumeSeptActivity.AAType;
import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.service.ConsumptionService;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ConsumptionViewActivity extends Activity {

	private LinearLayout layoutParticipant;
	private LinearLayout layoutPartHead;
	
	private TextView viewTeamName;
	private TextView viewConsumeName;
	private TextView viewConsumePayOFF;
	private TextView viewShowConsumePayoff;
	private TextView viewConsumeCategory;
	private TextView viewConsumeTotal;
	private TextView viewConsumeAvg;
	private TextView viewPartNum;
	private TextView viewShowAdvanceMoney;
	private CheckBox cboxPayOFF;
	private Button btnPaymentAdd;
	private Button btnPaymentConfirm;
	private Button btnPaymentCount;
	private Button btnPaymentSettlement;
	private ActionBar actionBar;
	private AApayDBHelper dbHelper;
	private ParticipantDao participantDao;
	private PaymentDao paymentDao;
	private ConsumptionDao consumptionDao;
	private List<Map<String, String>> payments;
	private List<Map<String, String>> participantList;
	private AAType aaType;
	
	private Consumption consumption;
	private String teamName;
	private ConsumptionService consumptionService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_consume_payment);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		dbHelper = new AApayDBHelper(this);
		participantDao = new ParticipantDao(dbHelper);
		paymentDao = new PaymentDao(dbHelper);
		consumptionDao = new ConsumptionDao(dbHelper);
		
		consumptionService = ConsumptionService.getInstance();
		initView();
		
		btnPaymentConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					ConsumptionViewActivity.this.finish();
			}
		});
		
		btnPaymentSettlement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cboxPayOFF.isChecked()) {
				}
				else {
					cboxPayOFF.toggle();
				}
				consumptionDao.updatePayOffById(consumption.getId());
				Toast.makeText(ConsumptionViewActivity.this, "已经结算", Toast.LENGTH_SHORT).show();
				ConsumptionViewActivity.this.finish();
			}
		});
	}
	
	/**
	 * 初始化界面
	 */
	private void initData() {
		Intent intent = getIntent();
		consumption = (Consumption)intent.getSerializableExtra("consumption");
		teamName = intent.getStringExtra("teamName");
		
		participantList = participantDao.getParticipantList(consumption.getTeamId());
		if(participantList.size() != 0) {
			payments = paymentDao.getPayments(consumption.getId());
			aaType = AAType.HavaPart;
		}
		else {
			aaType = AAType.NoPart;
		}
	}
	/**
	 * 初始化界面
	 */
	private void initView() {
		
		initData();		
		
		layoutParticipant = (LinearLayout) 
				findViewById(R.id.consume_layout_participant);
		layoutPartHead = (LinearLayout) findViewById(R.id.payment_layout_part_head);
		
		viewTeamName = (TextView) findViewById(R.id.payment_view_team_name);
		viewConsumeName = (TextView) findViewById(R.id.payment_view_consume_name);
		viewConsumePayOFF = (TextView) findViewById(R.id.payment_view_payoff);
		viewShowConsumePayoff = (TextView) findViewById(R.id.payment_view_show_payoff);
		viewConsumeCategory = (TextView) findViewById(R.id.payment_view_category);
		viewConsumeTotal = (TextView) findViewById(R.id.payment_view_total_amount);
		viewPartNum = (TextView) findViewById(R.id.payment_view_part_num);
		viewShowAdvanceMoney = (TextView) findViewById(R.id.payment_view_show_advance_money);
		viewConsumeAvg = (TextView) findViewById(R.id.payment_view_consume_avg);
		cboxPayOFF = (CheckBox) findViewById(R.id.payment_cbox_payoff);
		btnPaymentAdd = (Button) findViewById(R.id.payment_btn_add);
		btnPaymentConfirm = (Button) findViewById(R.id.payment_btn_confirm);
		btnPaymentCount = (Button) findViewById(R.id.payment_btn_count);
		btnPaymentSettlement = (Button) findViewById(R.id.payment_btn_settlement);
		initViewData();
		hideOrShowView();
	}
	
	/**
	 * 根据需求显示或隐藏组件
	 */
	private void hideOrShowView() {
		btnPaymentCount.setVisibility(View.GONE);
		btnPaymentAdd.setVisibility(View.GONE);
		if(consumption.getPayOFF().equals("是")) {
			viewConsumePayOFF.setVisibility(View.VISIBLE);
			viewShowConsumePayoff.setVisibility(View.VISIBLE);
			cboxPayOFF.setVisibility(View.GONE);
			btnPaymentSettlement.setVisibility(View.GONE);
		}
		else {
			viewConsumePayOFF.setVisibility(View.GONE);
			viewShowConsumePayoff.setVisibility(View.GONE);
			cboxPayOFF.setVisibility(View.VISIBLE);
			btnPaymentSettlement.setVisibility(View.VISIBLE);
		}
		
		if(aaType == AAType.HavaPart) {
			layoutPartHead.setVisibility(View.VISIBLE);
			layoutParticipant.setVisibility(View.VISIBLE);
		}
		else {
			layoutPartHead.setVisibility(View.GONE);
			layoutParticipant.setVisibility(View.GONE);
		}
	}
	/**
	 * 初始化界面数据
	 */
	private void initViewData() {
		if(consumption != null) {
			viewTeamName.setText(teamName);
			viewConsumeName.setText(consumption.getName());
			
			viewConsumeCategory.setText(consumption.getType());
			viewConsumeTotal.setText(String.valueOf(consumption.getMoney()));
			viewPartNum.setText(String.valueOf(consumption.getParticipantNum()));
			countConsumptionResult();
			if(aaType == AAType.HavaPart)
				showLayoutCountResult();
		}
	}
	
	/**
	 * 计算消费结果
	 */
	private void countConsumptionResult() {
		try {
			viewConsumeAvg.setText(String.valueOf(consumptionService.countAvg(consumption.getMoney(), consumption.getParticipantNum())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 展示计算后的结果
	 */
	private void showLayoutCountResult() {
		if(consumption == null) {
			return;
		}
		layoutParticipant.removeAllViews();
		viewShowAdvanceMoney.setText("结余情况");
		List<Map<String, String>> balanceList = consumptionService.countPartBanlance(consumption,payments);
		if(balanceList == null) {
			return;
		}
		for(Map<String, String> map : balanceList) {
			LinearLayout participant = new LinearLayout(ConsumptionViewActivity.this);
			LinearLayout.LayoutParams params = 
					new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			participant.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView viewName = new TextView(ConsumptionViewActivity.this);
			LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT,1f);
			viewParams.setMargins(40, 0, 0, 0);
			viewName.setLayoutParams(viewParams);
			viewName.setGravity(Gravity.CENTER_HORIZONTAL);
			viewName.setText(map.get("part_name"));
			
			TextView viewBalance = new TextView(ConsumptionViewActivity.this);
			
			viewBalance.setLayoutParams(new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));
			viewBalance.setGravity(Gravity.BOTTOM);
			
			viewBalance.setText("付："+map.get("payment")+"	结余："+map.get("balance"));
			
			participant.addView(viewName);
			participant.addView(viewBalance);
			layoutParticipant.addView(participant, params);
		}
		
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
			dbHelper = null;
		}
	}
}
