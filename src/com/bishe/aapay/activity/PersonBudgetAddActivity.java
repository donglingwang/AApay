package com.bishe.aapay.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.CategoryDao;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.dto.PersonalBudget;
import com.bishe.aapay.util.AApayUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class PersonBudgetAddActivity extends Activity {

	private ActionBar actionBar;
	private Spinner spinnerBudgetType;
	private EditText txtBudgetMoney;
	private Button btnCategory;
	private TextView viewCategoty;
	private TextView viewBudgetTime;
	private EditText txtRemark;
	private Button btnSave;
	private Button btnAgain;
	
	private String [] categoryNames;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private View mergeDateTimePicker;
	private String[] budgetTypes = {"新增开支","新增收入"};
	private int selectItem;
	private int selectedBudgetType;
	private AApayDBHelper dbHelper;
	private CategoryDao categoryDao;
	private PersonalBudgetDAO personalBudgetDAO;
	private PersonalBudget personalBudget;
	
	private AlertDialog.Builder dateTimeBuilder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.person_budget_activity);
		setContentView(R.layout.activity_add_budget);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		dbHelper = new AApayDBHelper(this);
		categoryDao = new CategoryDao(dbHelper);
		personalBudgetDAO = new PersonalBudgetDAO(dbHelper);
		initView();
		
		spinnerBudgetType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedBudgetType = position + 2;
				categoryNames = categoryDao.getCategoryNames(position+2);
				if(position == 0) {
					txtBudgetMoney.setTextColor(Color.BLUE);
				}
				else {
					txtBudgetMoney.setTextColor(Color.RED);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		/*
		 * 时间选择
		 */
		viewBudgetTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createDateTimeDialog();
			}
		});
		
		/*
		 * 类别选择
		 */
		btnCategory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createTypeSelectDialog();
			}
		});
		/*
		 * 保存
		 */
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!saveBudget()) {
					return;
				}
				Intent intent = new Intent(PersonBudgetAddActivity.this,PersonalBillActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private boolean saveBudget() {
		if(txtBudgetMoney.getText().toString().equals("")) {
			Toast.makeText(PersonBudgetAddActivity.this, "请输入消费金额！", Toast.LENGTH_SHORT).show();
			return false;
		}
		personalBudget = new PersonalBudget();
		personalBudget.setBudgetMoney(Double.parseDouble(txtBudgetMoney.getText().toString()));
		personalBudget.setBudgetType(selectedBudgetType);
		personalBudget.setBudgetCategory(categoryNames[selectItem]);
		personalBudget.setBudgetTime(getSelectedTime());
		personalBudget.setOperateTime(AApayUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		personalBudget.setRemark(txtRemark.getText().toString());
		personalBudget.setId(personalBudgetDAO.insert(personalBudget));
		Toast.makeText(PersonBudgetAddActivity.this, personalBudget.toString(), Toast.LENGTH_SHORT).show();
		return true;
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		spinnerBudgetType = (Spinner) findViewById(R.id.budget_spinner_type);
		spinnerBudgetType.setAdapter(new ArrayAdapter<String>(PersonBudgetAddActivity.this, 
				android.R.layout.simple_list_item_1, budgetTypes ));
		txtBudgetMoney = (EditText) findViewById(R.id.budget_txt_money);
		btnCategory = (Button) findViewById(R.id.budget_btn_category);
		viewCategoty = (TextView) findViewById(R.id.budget_view_category);
		viewBudgetTime = (TextView) findViewById(R.id.budget_view_time);
		txtRemark = (EditText) findViewById(R.id.budget_txt_remark);
		btnSave = (Button) findViewById(R.id.budget_btn_save);
		btnAgain = (Button) findViewById(R.id.budget_btn_again);
		
		viewBudgetTime.setText(AApayUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
		mergeDateTimePicker = getLayoutInflater().inflate(R.layout.date_time_merge, null,false);
		datePicker = (DatePicker) mergeDateTimePicker.findViewById(R.id.datePicker1);
		timePicker = (TimePicker) mergeDateTimePicker.findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
	}

	/**
	 * 创建Date和Time同时选择的控件
	 * @return
	 */
	private void createDateTimeDialog() {
		if(dateTimeBuilder != null) {
			return;
		}
		dateTimeBuilder = new AlertDialog.Builder(this);
		dateTimeBuilder.setTitle(R.string.date_time_select)
		.setView(mergeDateTimePicker)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				viewBudgetTime.setText(getSelectedTime());
				((ViewGroup)mergeDateTimePicker.getParent()).removeView(mergeDateTimePicker);
				dateTimeBuilder = null;
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((ViewGroup)mergeDateTimePicker.getParent()).removeView(mergeDateTimePicker);
				dateTimeBuilder = null;
			}
		})
	   .create().show();
	}
	
	/**
	 * 获取DateTimeDialog对话框显示的时间
	 */
	private String getSelectedTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 
				timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String date = sdf.format(calendar.getTime());
		return date;
	}
	
	/**
	 * 创建消费类型选择对话框
	 * @return
	 */
	private void createTypeSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.consume_type_select);
		builder.setSingleChoiceItems(categoryNames, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectItem = which;
			}
		});
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				viewCategoty.setText(categoryNames[selectItem]);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
	 builder.create().show();
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
