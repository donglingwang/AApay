package com.bishe.aapay.activity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.TeamAddActivity.ImportFlag;
import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.view.LvPartiNativeImportAdapter;
import com.bishe.aapay.view.LvPartiNativeImportAdapter.ViewHolder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ParticipantImportActivity extends Activity {

	private ListView lvPartImport;
	private Button btnConfirm;
	private ActionBar actionBar;
	private AApayDBHelper dbHelper;
	private TeamDao teamDao;
	private ParticipantDao participantDao;
	private LvPartiNativeImportAdapter lvPartiNativeImportAdapter;
	private List<Map<String, String>> partiList;
	private Intent intent;
	private ImportFlag importFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.participant_import_activity);
		setContentView(R.layout.activity_participant_import);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		dbHelper = new AApayDBHelper(this);
		teamDao = new TeamDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		intent = getIntent();
		importFlag = (ImportFlag) intent.getSerializableExtra("importFlag");
		
		initView();
		
		/*
		 * 点击确定返回结果
		 */
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.putExtra("selectedParts", lvPartiNativeImportAdapter.getSelectedPositons());
				ParticipantImportActivity.this.setResult(1, intent);
				ParticipantImportActivity.this.finish();
			}
		});
		
		lvPartImport.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				LvPartiNativeImportAdapter.ViewHolder holder = (ViewHolder) view.getTag();
				holder.cBox.toggle();
				LvPartiNativeImportAdapter.getIsSelected().put(position, holder.cBox.isChecked());
			}
		});
	}
	
	
	/**
	 * 初始化界面
	 */
	private void initView() {
		lvPartImport = (ListView) findViewById(R.id.team_list_import);
		btnConfirm = (Button) findViewById(R.id.team_btn_confirm);
		
		lvPartiNativeImportAdapter = new LvPartiNativeImportAdapter(this, participantDao,importFlag);
		lvPartImport.setAdapter(lvPartiNativeImportAdapter);
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
