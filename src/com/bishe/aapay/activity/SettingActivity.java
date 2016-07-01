package com.bishe.aapay.activity;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.CategoryDao;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.dto.User;
import com.bishe.aapay.service.AApayClient;
import com.bishe.aapay.util.AApayDateUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	private ActionBar actionBar;
	private Button btnTeamManage;
	private Button btnCategoryManage;
	private Button btnDataBackupManage;
	private Button btnBack;
	private Button btnLogin;
	private Button btnRegister;
	private Button btnRecovery;
	private ProgressBar barBackup;
	private ProgressBar barRecovery;
	
	private LinearLayout layoutLogin;
	private LinearLayout layoutNoLogin;
	private ImageView imgUserPhoto;
	private TextView viewUserName;
	private User user;
	private SharedPreferences preferences;
	
	private AApayDBHelper dbHelper;
	private CategoryDao categoryDao;
	private ConsumptionDao consumptionDao;
	private ParticipantDao participantDao;
	private PaymentDao paymentDao;
	private PersonalBudgetDAO personalBudgetDAO;
	private TeamDao teamDao;
	private SettingThread settingThread;
	private RecoveryThread recoveryThread;
	private Handler completeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0x126) {
				barBackup.setVisibility(View.GONE);
				btnDataBackupManage.setVisibility(View.VISIBLE);
			}
			else if(msg.what == 0x128) {
				barRecovery.setVisibility(View.GONE);
				btnRecovery.setVisibility(View.VISIBLE);
			}
			super.handleMessage(msg);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setTitle(R.string.setting_activity);
		setContentView(R.layout.activity_setting);
		preferences = getSharedPreferences("loginFlag", MODE_PRIVATE);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("user");
		
		dbHelper = new AApayDBHelper(this);
		categoryDao = new CategoryDao(dbHelper);
		consumptionDao = new ConsumptionDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		paymentDao = new PaymentDao(dbHelper);
		personalBudgetDAO = new PersonalBudgetDAO(dbHelper);
		teamDao = new TeamDao(dbHelper);
		
		settingThread = new SettingThread();
		settingThread.start();
		recoveryThread = new RecoveryThread();
		recoveryThread.start();
		initView();
		
		/*
		 * 登录
		 */
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		/*
		 * 注册
		 */
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		/*
		 * 打开团队管理界面
		 */
		btnTeamManage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,TeamManageActivity.class);
				startActivity(intent);
			}
		});
		
		/*
		 * 打开类别管理界面
		 */
		btnCategoryManage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,CategoryManageActivity.class);
				startActivity(intent);
				
			}
		});
		
		/*
		 * 数据备份
		 */
		btnDataBackupManage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String isLogin = preferences.getString("islogin", "no");
				if(isLogin.equals("yes")) {
					Message msg = new Message();
					msg.what = 0x125;
					int userId = preferences.getInt("userid", 0);
					Bundle bundle = new Bundle();
					bundle.putInt("userid", userId);
					msg.setData(bundle);
					settingThread.mHandler.sendMessage(msg);
					Toast.makeText(SettingActivity.this, "正在备份ing...", Toast.LENGTH_SHORT).show();
					barBackup.setVisibility(View.VISIBLE);
					btnDataBackupManage.setVisibility(View.GONE);
				}
				else {
					Toast.makeText(SettingActivity.this, "请先登录...", Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*
		 * 数据恢复
		 */
		btnRecovery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String isLogin = preferences.getString("islogin", "no");
				if(isLogin.equals("yes")) {
					barRecovery.setVisibility(View.VISIBLE);
					btnRecovery.setVisibility(View.GONE);
					Message msg = new Message();
					msg.what = 0x127;
					int userId = preferences.getInt("userid", 0);
					Bundle bundle = new Bundle();
					bundle.putInt("userid", userId);
					msg.setData(bundle);
					recoveryThread.mHandler.sendMessage(msg);
					Toast.makeText(SettingActivity.this, "正在恢复ing...", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(SettingActivity.this, "请先登录...", Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*
		 * 返回
		 */
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,ConsumeActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 初始化界面
	 */
	private void initView() {
		layoutLogin = (LinearLayout) findViewById(R.id.setting_layout_login);
		layoutNoLogin = (LinearLayout) findViewById(R.id.setting_layout_nologin);
		imgUserPhoto = (ImageView) findViewById(R.id.setting_user_photo);
		viewUserName = (TextView) findViewById(R.id.setting_user_name);
		
		btnTeamManage = (Button) findViewById(R.id.setting_btn_team);
		btnCategoryManage = (Button) findViewById(R.id.setting_btn_category);
		btnDataBackupManage = (Button) findViewById(R.id.setting_btn_data_backup);
		btnBack = (Button) findViewById(R.id.setting_btn_back);
		btnRecovery = (Button) findViewById(R.id.setting_btn_data_recovery);
		barBackup = (ProgressBar) findViewById(R.id.setting_bar_backup);
		barRecovery = (ProgressBar) findViewById(R.id.setting_bar_recovery);
		btnLogin = (Button) findViewById(R.id.setting_btn_login);
		btnRegister = (Button) findViewById(R.id.setting_btn_register);
		String isLogin = preferences.getString("islogin", "no");
		String userName = preferences.getString("username", "");
		if(isLogin.equals("yes")) {
			layoutLogin.setVisibility(View.GONE);
			layoutNoLogin.setVisibility(View.VISIBLE);
			viewUserName.setText(userName);
		}
		else {
			layoutLogin.setVisibility(View.VISIBLE);
			layoutNoLogin.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(SettingActivity.this,ConsumeActivity.class);
			startActivity(intent);
			break;
		case R.id.change_account:
			Intent intent2 = new Intent(SettingActivity.this,LoginActivity.class);
			startActivity(intent2);
		}
		return super.onOptionsItemSelected(item);
	}
	
	class SettingThread extends Thread {
		public Handler mHandler;
		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					Bundle bundle = msg.getData();
					int userId = bundle.getInt("userid");
					if(msg.what == 0x125) {
						if(AApayClient.getInstance().backupCategory(categoryDao.findAll(),userId)) {
						}
						AApayClient.getInstance().backupConsumption(consumptionDao.findAll(), userId);
						AApayClient.getInstance().backupParticipant(participantDao.findAll(), userId);
						AApayClient.getInstance().backupPayment(paymentDao.findAll(), userId);
						AApayClient.getInstance().backupBudget(personalBudgetDAO.findAll(), userId);
						AApayClient.getInstance().backupTeam(teamDao.findAll(), userId);
						Toast.makeText(SettingActivity.this, "备份成功！！！", Toast.LENGTH_SHORT).show();
						completeHandler.sendEmptyMessage(0x126);
						
					}
					else if(msg.what == 0x127) {
						AApayClient.getInstance().dataRecovery(categoryDao, consumptionDao, participantDao, 
								paymentDao, personalBudgetDAO, teamDao, userId);
						Toast.makeText(SettingActivity.this, "恢复成功！！！", Toast.LENGTH_SHORT).show();
						completeHandler.sendEmptyMessage(0x128);
					}
					super.handleMessage(msg);
				}
			};
			Looper.loop();
			super.run();
		}
	}
	
	class RecoveryThread extends Thread {
		public Handler mHandler;
		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					Bundle bundle = msg.getData();
					int userId = bundle.getInt("userid");
					if(msg.what == 0x127) {
						AApayClient.getInstance().dataRecovery(categoryDao, consumptionDao, participantDao, 
								paymentDao, personalBudgetDAO, teamDao, userId);
						Toast.makeText(SettingActivity.this, "恢复成功！！！", Toast.LENGTH_SHORT).show();
						completeHandler.sendEmptyMessage(0x128);
					}
					super.handleMessage(msg);
				}
			};
			Looper.loop();
			super.run();
		}
	}
}
