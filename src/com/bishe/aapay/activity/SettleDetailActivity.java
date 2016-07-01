package com.bishe.aapay.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dto.SettlementDTO;
import com.bishe.aapay.view.LvConsumeDetailAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettleDetailActivity extends Activity {

	private ActionBar actionBar;
	private TextView viewPartName;
	private TextView viewConsumeNum;
	private TextView viewConsumeMoney;
	private TextView viewConsumePay;
	private TextView viewConsumeOwe;
	private ListView lvConsumeDetail;
	private Button btnConfirm;
	private Button btnSendMsg;
	private AApayDBHelper dbHelper;
	private PaymentDao paymentDao;
	private ParticipantDao participantDao;
	private long partId;
	private ArrayList<Long> consumptionIds;
	private SettlementDTO settlementDTO;
	private List<Map<String, String>> consumptionList;
	private StringBuffer message = null;
	private SmsManager sManager;
	private LvConsumeDetailAdapter lvConsumeDetailAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.settle_detail_activity);
		setContentView(R.layout.activity_settle_part_detail);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		sManager = SmsManager.getDefault();
		Intent intent = getIntent();
		partId = intent.getLongExtra("part_id", 1);
		consumptionIds = (ArrayList<Long>)intent.getSerializableExtra("ids");
		settlementDTO = (SettlementDTO) intent.getSerializableExtra("settlementDto");
		
		dbHelper = new AApayDBHelper(this);
		paymentDao = new PaymentDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		consumptionList = paymentDao.getConsumptionBypartId(partId, consumptionIds);
		
		initView();
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettleDetailActivity.this.finish();
			}
		});
		
		btnSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessage();
				// ����һ��PendingIntent����
				PendingIntent pi = PendingIntent.getActivity(
					SettleDetailActivity.this, 0, new Intent(), 0);
				// ���Ͷ���
				//Toast.makeText(SettleDetailActivity.this, participantDao.getPhone(partId), Toast.LENGTH_SHORT).show();
				/*sManager.sendTextMessage(participantDao.getPhone(partId),
					null, message.toString(), pi, null);*/
				/*Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setData(Uri.parse("content://mms-sms/"));
				startActivity(intent);*/
				Uri smsToUri = Uri.parse("smsto:"+participantDao.getPhone(partId));
				Intent intent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
				intent.putExtra("sms_body", message.toString());
				startActivity( intent );
				// ��ʾ���ŷ������
				Toast.makeText(SettleDetailActivity.this, "���ŷ������", 8000).show();
				
			}
		});
	}
	
	/**
	 * ��ʼ������
	 */
	private void initView() {
		viewPartName = (TextView) findViewById(R.id.settle_detail_part_name);
		viewConsumeNum = (TextView) findViewById(R.id.settle_detail_consume_num);
		viewConsumeMoney = (TextView) findViewById(R.id.settle_detail_consume_money);
		viewConsumePay = (TextView) findViewById(R.id.settle_detail_consume_pay);
		viewConsumeOwe = (TextView) findViewById(R.id.settle_detail_consume_owe);
		
		lvConsumeDetail = (ListView) findViewById(R.id.settle_lv_consume_detail);
		btnConfirm = (Button) findViewById(R.id.settle_detail_confirm);
		btnSendMsg = (Button) findViewById(R.id.settle_detail_send_msg);
		
		viewPartName.setText(participantDao.getPartName(partId));
		viewConsumeNum.setText(String.valueOf(consumptionList.size()));
		viewConsumeMoney.setText(String.valueOf(settlementDTO.getTotalMoney()));
		viewConsumePay.setText(String.valueOf(settlementDTO.getTotalAdvanceMoney()));
		viewConsumeOwe.setText(String.valueOf(settlementDTO.getTotalOweMoney()));
		
		lvConsumeDetailAdapter = new LvConsumeDetailAdapter(this, consumptionList);
		lvConsumeDetail.setAdapter(lvConsumeDetailAdapter);
	}

	private void sendMessage() {
		message = new StringBuffer();
		message.append(participantDao.getPartName(partId)+"\n");
		message.append("�㹲���� "+consumptionList.size()+" ��\n");
		message.append("�ܹ������� "+settlementDTO.getTotalMoney()+" Ԫ\n");
		message.append("�ܹ��渶�� "+settlementDTO.getTotalAdvanceMoney()+" Ԫ\n");
		message.append("���ڽ��� "+settlementDTO.getTotalOweMoney()+" Ԫ\n");
		message.append("��������������£�\n\n");
		for(Map<String, String> map : consumptionList) {
			message.append(map.get("consumption_time")+" �μ� "+map.get("consumption_name")+"\n");
			message.append("�����ܶ�Ϊ��"+map.get("consumption_money")+"\n ������ԱΪ��"+map.get("part_num")+"\n");
			message.append("���渶�� "+map.get("advance_money")+"\n");
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
		if(dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
		super.onDestroy();
		
	}
}
