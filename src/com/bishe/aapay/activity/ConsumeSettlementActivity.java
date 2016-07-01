package com.bishe.aapay.activity;

import java.util.List;
import java.util.Map;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.dto.SettlementDTO;
import com.bishe.aapay.view.LvSettlePaymentAdapter;
import com.bishe.aapay.view.LvSettlePaymentAdapter.ViewHolder;
import com.bishe.aapay.view.LvSettleResultAdapter;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumeSettlementActivity extends Activity {

	private ActionBar actionBar;
	private Spinner spinnerTeamNames;
	private TextView viewTotalMoney;
	private ListView lvSettletmentPayment;
	private ListView lvSettlementResult;
	private Button btnSettlement;
	private Button btnConfirm;
	private AApayDBHelper dbHelper;
	private ConsumptionDao consumptionDao;
	private PaymentDao paymentDao;
	private ParticipantDao participantDao;
	private TeamDao teamDao;
	private String [] teamNames;
	private List<Map<String, String>> teamLists;
	private long teamId;
	private int teamPosition;
	private LvSettlePaymentAdapter lvSettlePaymentAdapter;
	private LvSettleResultAdapter lvSettleResultAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.consume_settlement);
		setContentView(R.layout.activity_settlement);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		teamPosition = intent.getIntExtra("teamPosition", 0);
		
		dbHelper = new AApayDBHelper(this);
		consumptionDao = new ConsumptionDao(dbHelper);
		paymentDao = new PaymentDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		teamDao = new TeamDao(dbHelper);
		initView();
		
		/*
		 * 选择不同的团队进行产看spinner
		 */
		spinnerTeamNames.setSelection(teamPosition);
		spinnerTeamNames.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				teamId = Long.parseLong(teamLists.get(position).get("team_id"));
				lvSettlePaymentAdapter.setTeamId(teamId);
				lvSettlePaymentAdapter.notifyDataSetChanged();
				lvSettleResultAdapter.setSelectedConsumptionId(LvSettlePaymentAdapter.getSelectedPositons());
				lvSettleResultAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		/*
		 * 显示未结算账单的ListView
		 */
		lvSettletmentPayment.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				LvSettlePaymentAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				holder.cBox.toggle();
				
				LvSettlePaymentAdapter.getIsSelected().put(position, holder.cBox.isChecked());
				lvSettleResultAdapter.setSelectedConsumptionId(LvSettlePaymentAdapter.getSelectedPositons());
				lvSettleResultAdapter.notifyDataSetChanged();
				viewTotalMoney.setText(String.valueOf(consumptionDao.getMoneyByIds(LvSettlePaymentAdapter.getSelectedPositons())));
			}
		});
		/*
		 * 显示计算结果的ListView
		 */
		lvSettlementResult.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int position, long arg3) {
					Intent intent = new Intent(ConsumeSettlementActivity.this,SettleDetailActivity.class);
					intent.putExtra("settlementDto",(SettlementDTO)lvSettleResultAdapter.getItem(position));
					intent.putExtra("part_id", lvSettleResultAdapter.getItemId(position));
					intent.putExtra("ids", LvSettlePaymentAdapter.getSelectedPositons());
					startActivity(intent);
				}
		});
		/*
		 * 点击结算按钮
		 */
		btnSettlement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(lvSettlePaymentAdapter.getCount() < 1) {
					Toast.makeText(ConsumeSettlementActivity.this, "没有需要结算的账单！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(ConsumeSettlementActivity.this,ConsumeActivity.class);
					startActivity(intent);
					return;
				}
				if(LvSettlePaymentAdapter.getSelectedPositons().size() < 1) {
					Toast.makeText(ConsumeSettlementActivity.this, "请选择需要结算的账单！", Toast.LENGTH_SHORT).show();
					return;
				}
				consumptionDao.updatePayOffByIds(LvSettlePaymentAdapter.getSelectedPositons());
				Toast.makeText(ConsumeSettlementActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
				lvSettlePaymentAdapter.resetData();
				lvSettlePaymentAdapter.notifyDataSetChanged();
				lvSettleResultAdapter.setSelectedConsumptionId(LvSettlePaymentAdapter.getSelectedPositons());
				lvSettleResultAdapter.notifyDataSetChanged();
				viewTotalMoney.setText(String.valueOf(consumptionDao.getMoneyByIds(LvSettlePaymentAdapter.getSelectedPositons())));
			}
		});
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConsumeSettlementActivity.this,ConsumeActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
	/**
	 * 初始化spinner数据
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
		spinnerTeamNames = (Spinner) findViewById(R.id.consume_spin_team);
		spinnerTeamNames.setAdapter(new ArrayAdapter<String>(ConsumeSettlementActivity.this, 
				android.R.layout.simple_list_item_1, teamNames));
		lvSettletmentPayment = (ListView) findViewById(R.id.consume_lv_settle_payment);
		lvSettlementResult = (ListView) findViewById(R.id.consume_lv_settle_result);
		viewTotalMoney = (TextView) findViewById(R.id.consume_view_total_money);
		btnSettlement = (Button) findViewById(R.id.consume_btn_settle);
		btnConfirm = (Button) findViewById(R.id.consume_btn_confirm);
		
		lvSettlePaymentAdapter = new LvSettlePaymentAdapter(this, consumptionDao, teamId);
		lvSettletmentPayment.setAdapter(lvSettlePaymentAdapter);
		
		viewTotalMoney.setText(String.valueOf(0));
		lvSettleResultAdapter = new LvSettleResultAdapter(this, LvSettlePaymentAdapter.getSelectedPositons(), 
				paymentDao, participantDao, consumptionDao);
		lvSettlementResult.setAdapter(lvSettleResultAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			Intent intent = new Intent(ConsumeSettlementActivity.this,ConsumeActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		if(dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
		super.onDestroy();
	}
	
}


