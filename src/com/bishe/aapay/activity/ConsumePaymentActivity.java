package com.bishe.aapay.activity;

import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.ConsumeSeptActivity.AAType;
import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.dto.Payment;
import com.bishe.aapay.service.ConsumptionService;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout.Alignment;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumePaymentActivity extends Activity {

	private LinearLayout layoutParticipant;
	private LinearLayout layoutPartHead;
	
	private TextView viewTeamName;
	private TextView viewConsumeName;
	private TextView viewConsumePayoff;
	private TextView viewConsumeCategory;
	private TextView viewConsumeTotal;
	private TextView viewConsumeAvg;
	private TextView viewPartNum;
	private TextView viewShowAdvanceMoney;
	private CheckBox cboxPayOFF;
	private Button btnPaymentAdd;
	private Button btnPaymentConfirm;
	private Button btnPaymentCount;
	private Button btnPaymentSettletment;
	private EditText [] paymentTexts;
	private ActionBar actionBar;
	
	private Consumption consumption;
	
	private ConsumptionService consumptionService;
	
	private long consumptionId = 0;
	private AApayDBHelper dbHelper;
	private ConsumptionDao consumptionDao;
	private PaymentDao paymentDao;
	private AAType aaType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setTitle(R.string.consume_payment_activity);
		setContentView(R.layout.activity_consume_payment);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		consumption = (Consumption) intent.getSerializableExtra("consumption");
		aaType = (AAType) intent.getSerializableExtra("aaType");
		consumptionService = ConsumptionService.getInstance();
		
		dbHelper = new AApayDBHelper(this);
		consumptionDao = new ConsumptionDao(dbHelper);
		paymentDao = new PaymentDao(dbHelper);
		initView();
		/*
		 * 继续添加AA制记账事项
		 */
		btnPaymentAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!saveConsumptionInfo())
					return;
				Intent intent = new Intent(ConsumePaymentActivity.this,ConsumeSeptActivity.class);
				startActivity(intent);
			}
		});
		/*
		 * 计算人均支付额
		 */
		btnPaymentCount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(consumption == null) {
					return;
				}
				
				for(int i = 0; i < paymentTexts.length; i++) {
					consumption.getParticipantList().get(i).put("payment", paymentTexts[i].getText().toString());
				}
				if(consumptionService.checkPayment(consumption)) {
					showLayoutCountResult();
				}
				else {
					Toast.makeText(ConsumePaymentActivity.this, "垫付额和消费总额不符合！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		/*
		 * 确定按钮
		 */
		btnPaymentConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!saveConsumptionInfo())
					return;
				Intent intent = new Intent(ConsumePaymentActivity.this,ConsumeActivity.class);
				startActivity(intent);
			}
		});
		/*
		 * 结算按钮
		 */
		btnPaymentSettletment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!saveConsumptionInfo())
					return;
				Intent intent = new Intent(ConsumePaymentActivity.this,ConsumeSettlementActivity.class);
				intent.putExtra("teamPosition", 0);
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
	 * 保存前做校验
	 */
	private boolean saveConsumptionInfo() {
		if(consumption == null) {
			return false;
		}
		
		if(cboxPayOFF.isChecked()) {
			consumption.setPayOFF("是");
		}
		else {
			consumption.setPayOFF("否");
		}
		
		if(aaType == AAType.HavaPart) {
			for(int i = 0; i < paymentTexts.length; i++) {
				consumption.getParticipantList().get(i).put("payment", paymentTexts[i].getText().toString());
			}
			if(!consumptionService.checkPayment(consumption)) {
				Toast.makeText(ConsumePaymentActivity.this, "垫付额和消费总额不符合！", Toast.LENGTH_SHORT).show();
				return false;
			}
			Toast.makeText(ConsumePaymentActivity.this, String.valueOf(consumptionId), Toast.LENGTH_SHORT).show();
			if(consumptionId != 0) {
				consumptionDao.update(consumption);
			}
			else {
				consumptionId = consumptionDao.insert(consumption);
				consumption.setId(consumptionId);
				List<Payment> payments = consumptionService.getPaymentByConsumption(consumption, consumptionId);
				paymentDao.insertList(payments);
			}
		}
		else {
			if(consumptionId != 0) {
				consumptionDao.update(consumption);
			}
			else {
				consumptionId = consumptionDao.insert(consumption);
				consumption.setId(consumptionId);
			}
		}
		return true;
	}
	/**
	 * 初始化界面
	 */
	private void initView() {
		layoutParticipant = (LinearLayout) 
				findViewById(R.id.consume_layout_participant);
		layoutPartHead = (LinearLayout) findViewById(R.id.payment_layout_part_head);
		
		viewTeamName = (TextView) findViewById(R.id.payment_view_team_name);
		viewConsumeName = (TextView) findViewById(R.id.payment_view_consume_name);
		viewConsumePayoff = (TextView) findViewById(R.id.payment_view_payoff);
		viewConsumeCategory = (TextView) findViewById(R.id.payment_view_category);
		viewConsumeTotal = (TextView) findViewById(R.id.payment_view_total_amount);
		viewPartNum = (TextView) findViewById(R.id.payment_view_part_num);
		viewShowAdvanceMoney = (TextView) findViewById(R.id.payment_view_show_advance_money);
		viewConsumeAvg = (TextView) findViewById(R.id.payment_view_consume_avg);
		cboxPayOFF = (CheckBox) findViewById(R.id.payment_cbox_payoff);
		btnPaymentAdd = (Button) findViewById(R.id.payment_btn_add);
		btnPaymentConfirm = (Button) findViewById(R.id.payment_btn_confirm);
		btnPaymentCount = (Button) findViewById(R.id.payment_btn_count);
		btnPaymentSettletment = (Button) findViewById(R.id.payment_btn_settlement);
		
		if(aaType == AAType.NoPart) {
			btnPaymentCount.setVisibility(View.GONE);
			btnPaymentSettletment.setVisibility(View.GONE);
		}
		else {
			btnPaymentCount.setVisibility(View.VISIBLE);
			btnPaymentSettletment.setVisibility(View.VISIBLE);
		}
		
		if(consumption != null && consumption.getParticipantList() == null) {
			layoutParticipant.setVisibility(View.GONE);
			layoutPartHead.setVisibility(View.GONE);
		} else 
		{
			layoutParticipant.setVisibility(View.VISIBLE);
			layoutPartHead.setVisibility(View.VISIBLE);
			initLayoutParticipant();
		}
		 initViewByConsumption();
		 
	}

	/**
	 * 根据传递的Consumption对象
	 * 初始化基本组件
	 */
	private void initViewByConsumption() {
		if(consumption != null) {
			viewTeamName.setText(consumption.getTeamName());
			viewConsumeName.setText(consumption.getName());
			viewConsumeCategory.setText(consumption.getType());
			viewConsumeTotal.setText(String.valueOf(consumption.getMoney()));
			viewPartNum.setText(String.valueOf(consumption.getParticipantNum()));
			countConsumptionResult();
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
	 * 初始化参与人员
	 */
	private void initLayoutParticipant() {
		if(consumption != null) {
			paymentTexts = new EditText[consumption.getParticipantList().size()];
			layoutParticipant.removeAllViews();
			for(int i = 0; i < consumption.getParticipantList().size(); i++) {
				LinearLayout participant = new LinearLayout(ConsumePaymentActivity.this);
				LinearLayout.LayoutParams params = 
						new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				participant.setOrientation(LinearLayout.HORIZONTAL);
				
				TextView viewName = new TextView(ConsumePaymentActivity.this);
				LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0,
						LinearLayout.LayoutParams.WRAP_CONTENT,1f);
				viewParams.setMargins(40, 0, 0, 0);
				viewName.setLayoutParams(viewParams);
				viewName.setGravity(Gravity.BOTTOM);
				viewName.setText(String.valueOf(consumption.getParticipantList().get(i).get("part_name")));
				
				
				EditText txtMoney = new EditText(ConsumePaymentActivity.this);
				txtMoney.setLayoutParams(new LinearLayout.LayoutParams(
						0,
						LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
				txtMoney.setKeyListener(new DigitsKeyListener(true,true));
				txtMoney.setGravity(Gravity.CENTER_HORIZONTAL);
				
				participant.addView(viewName);
				participant.addView(txtMoney);
				paymentTexts[i] = txtMoney;
				layoutParticipant.addView(participant, params);
			}
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
		List<Map<String, String>> balanceList = consumptionService.countPartBanlance(consumption);
		if(balanceList == null) {
			return;
		}
		for(final Map<String, String> map : balanceList) {
			LinearLayout participant = new LinearLayout(ConsumePaymentActivity.this);
			LinearLayout.LayoutParams params = 
					new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			participant.setOrientation(LinearLayout.HORIZONTAL);
			
			TextView viewName = new TextView(ConsumePaymentActivity.this);
			LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT,1f);
			viewParams.setMargins(40, 0, 0, 0);
			viewName.setLayoutParams(viewParams);
			viewName.setGravity(Gravity.CENTER_HORIZONTAL);
			viewName.setText(map.get("part_name"));
			
			TextView viewBalance = new TextView(ConsumePaymentActivity.this);
			
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
	protected void onResume() {		
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
