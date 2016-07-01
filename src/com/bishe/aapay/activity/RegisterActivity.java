package com.bishe.aapay.activity;

import com.bishe.aapay.dto.User;
import com.bishe.aapay.service.AApayClient;
import com.bishe.aapay.util.AApayUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private ActionBar actionBar;
	private EditText txtMaill;
	private EditText txtName;
	private EditText txtPassword;
	private Button btnRegister;
	private  User oldUser;
	
	private RegisterThread registerThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.register_activity);
		setContentView(R.layout.activity_register);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		initView();
		registerThread = new RegisterThread();
		registerThread.start();
		
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 0x123;
				User user = new User();
				user.setPassword(txtPassword.getText().toString());
				user.setUserName(txtName.getText().toString());
				user.setUserMail(txtMaill.getText().toString());
				user.setRegisterDate(AApayUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				msg.setData(bundle);
				registerThread.mHandler.sendMessage(msg);
				
			}
		});
		
	}
	
	private void initView() {
		txtMaill = (EditText) findViewById(R.id.register_txt_mail);
		txtName = (EditText) findViewById(R.id.register_txt_name);
		txtPassword = (EditText) findViewById(R.id.register_txt_password);
		btnRegister = (Button) findViewById(R.id.register_btn);
		
	}

	/**
	 * 校验并
	 * 保存用户信息
	 */
	private void saveUser() {
		if(txtMaill.getText().toString().equals("")||txtName.getText().toString().equals("")
				||txtPassword.getText().toString().equals("")) {
			Toast.makeText(RegisterActivity.this, "不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!AApayUtil.checkEmail(txtMaill.getText().toString())) {
			Toast.makeText(RegisterActivity.this, "请输入正确的邮箱格式！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		new Thread(new Runnable() {
			public void run() {
				//AApayClient.getInstance().sendUser(user);
				oldUser = AApayClient.getInstance().readUser(txtMaill.getText().toString());
				Toast.makeText(RegisterActivity.this,oldUser.getPassword()+" "+oldUser.getUserName(), Toast.LENGTH_SHORT).show();
			}
		}).start();
		 
		
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
	
	class RegisterThread extends Thread {
		public Handler mHandler;
		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					if(msg.what == 0x123) {
						oldUser = AApayClient.getInstance().readUser(txtMaill.getText().toString());
						if(oldUser != null) {
							Toast.makeText(RegisterActivity.this,oldUser.getUserMail()+" 邮箱已经注册！", Toast.LENGTH_SHORT).show();
							return;
						}
						User user = (User) msg.getData().getSerializable("user");
						if(AApayClient.getInstance().sendUser(user)) {
							Toast.makeText(RegisterActivity.this,"注册成功,请登录！", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					}
				};
			};
			Looper.loop();
			super.run();
		}
	}
}
