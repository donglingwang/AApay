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
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private ActionBar actionBar;
	private Button btnLogin;
	private EditText txtMail;
	private EditText txtPassword;
	private LoginThread loginThread;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	
	private AApayDBHelper dbHelper;
	private CategoryDao categoryDao;
	private ConsumptionDao consumptionDao;
	private ParticipantDao participantDao;
	private PaymentDao paymentDao;
	private PersonalBudgetDAO personalBudgetDAO;
	private TeamDao teamDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.login_activity);
		setContentView(R.layout.activity_login);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		preferences = getSharedPreferences("loginFlag", MODE_PRIVATE);
		editor = preferences.edit();
		
		dbHelper = new AApayDBHelper(this);
		categoryDao = new CategoryDao(dbHelper);
		consumptionDao = new ConsumptionDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		paymentDao = new PaymentDao(dbHelper);
		personalBudgetDAO = new PersonalBudgetDAO(dbHelper);
		teamDao = new TeamDao(dbHelper);
		initView();
		loginThread = new LoginThread();
		loginThread.start();
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 0x124;
				Bundle bundle = new Bundle();
				bundle.putString("email", txtMail.getText().toString());
				bundle.putString("password", txtPassword.getText().toString());
				msg.setData(bundle);
				loginThread.mHandler.sendMessage(msg);
			}
		});
	}
	
	
	private void initView() {
		txtMail = (EditText) findViewById(R.id.login_txt_mail);
		txtPassword = (EditText) findViewById(R.id.login_txt_password);
		btnLogin = (Button) findViewById(R.id.login_btn);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.register:
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class LoginThread extends Thread {
		public Handler mHandler;
		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler (){
				public void handleMessage(android.os.Message msg) {
					if(msg.what == 0x124) {
						Bundle bundle = msg.getData();
						String email = bundle.getString("email");
						String password = bundle.getString("password");
						User user = AApayClient.getInstance().readUser(email);
						if(user == null) {
							Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
							return;
						}
						if(!password.equals(user.getPassword())) {
							Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
							return;
						}
						Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
						editor.putString("islogin", "yes");
						editor.putString("usermail", user.getUserMail());
						editor.putString("username", user.getUserName());
						editor.putInt("userid", user.getUserId());
						editor.commit();
						Toast.makeText(LoginActivity.this, "正在加载数据，请稍等！！！", Toast.LENGTH_SHORT).show();
						AApayClient.getInstance().dataRecovery(categoryDao, consumptionDao, participantDao, 
								paymentDao, personalBudgetDAO, teamDao, user.getUserId());
						Intent intent = new Intent(LoginActivity.this,SettingActivity.class);
						intent.putExtra("user", user);
						startActivity(intent);
					}
				};
			};
			Looper.loop();
			super.run();
			
		}
	}
	
}
