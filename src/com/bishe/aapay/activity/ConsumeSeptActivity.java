package com.bishe.aapay.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.CategoryDao;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.util.AApayUtil;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.os.Handler;
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

public class ConsumeSeptActivity extends Activity {

	private Spinner spinnerTeamName;
	private Spinner spinnerConsumeMode;
	
	private EditText txtConsumeName;
	private EditText txtConsumeMoney;
	private EditText txtRemark;
	private EditText txtTeamNum;
	private TextView viewPeopleChoice;
	private TextView viewType;
	private TextView viewDate;
	private TextView viewConsumeMode;
	private Button btnTypeSelect;
	private Button btnPeopleSelect;
	private Button btnContinue;
	
	private DatePicker datePicker;
	private TimePicker timePicker;
	private View mergeDateTimePicker;
	
	private ActionBar actionBar;
	private AApayDBHelper dbHelper;
	private TeamDao teamDao;
	private ParticipantDao participantDao;
	private CategoryDao categoryDao;
	private String [] teamNames;
	private ArrayList<Map<String, String>> teamList;
	private String [] participantNames;
	private List<Map<String, String>> participantList;
	
	private String [] categoryNames;
	String [] modes = new String [] {
			"分开","平均"
	};
	
	private long selectedTeamId;
	private String selectedTeamName;
	private ArrayList<Integer> mSlectedItems;
	private Map<Integer, Boolean> mSelectedMap;
	//private ArrayList<Integer> selectedPartItems;
	private ArrayList<Map<String, String>> selectedPartMaps;
	private int selectItem = 0;
	
	private Consumption consumption;
	private List<Consumption> consumptions = new ArrayList<Consumption>();
	
	public  enum AAType {HavaPart,NoPart};
	private AAType aaType;
	public ConsumeSeptActivity() {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.consume_separate_activity);
		setContentView(R.layout.activity_consume_separate);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		dbHelper = new AApayDBHelper(this);
		teamDao = new TeamDao(dbHelper);
		participantDao = new ParticipantDao(dbHelper);
		categoryDao = new CategoryDao(dbHelper);
		consumption = new Consumption();

		
		initView();
		
		spinnerTeamName.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				if(participantDao.getParticipantList(Long.parseLong(teamList.get(position).get("team_id"))).size() == 0) {
					
					aaType = AAType.NoPart;
					
					btnPeopleSelect.setVisibility(View.GONE);
					txtTeamNum.setVisibility(View.VISIBLE);
					viewConsumeMode.setVisibility(View.VISIBLE);
					spinnerConsumeMode.setVisibility(View.GONE);
					txtTeamNum.setText(teamList.get(position).get("team_num"));
				}
				else {
					
					aaType = AAType.HavaPart;
					btnPeopleSelect.setVisibility(View.VISIBLE);
					txtTeamNum.setVisibility(View.GONE);
					viewConsumeMode.setVisibility(View.GONE);
					spinnerConsumeMode.setVisibility(View.VISIBLE);
				}
				viewPeopleChoice.setText("");
				getSelectedTeamInfo();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		/*
		 * 选择日期
		 */
		viewDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createDateTimeDialog().show();
			}
		});
		
		/*
		 * 参与人员选择
		 */
		btnPeopleSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createPeopleSelectDialog().show();
			}
		});
		
		/*
		 * 类型选择
		 */
		btnTypeSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createTypeSelectDialog().show();
			}
		});
		
		/*
		 * 继续，打开支付情况界面
		 */
		btnContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!saveConsumption()) {
					return;
				}
				Intent intent = new Intent(ConsumeSeptActivity.this,ConsumePaymentActivity.class);
				Bundle bundle = new Bundle();
				consumption.setTeamName(selectedTeamName);
				bundle.putSerializable("consumption", consumption);
				bundle.putSerializable("aaType", aaType);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 添加菜单的响应事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			Intent intent = new Intent(ConsumeSeptActivity.this,ConsumeActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	private void initData() {
		teamList = (ArrayList<Map<String, String>>) teamDao.getTeamList();
		teamNames = new String[teamList.size()];
		for(int i = 0; i < teamList.size(); i++) {
			teamNames[i] = teamList.get(i).get("team_name");
		}
		/*是否有参与人员*/
		if(participantDao.getParticipantList(Long.parseLong(teamList.get(0).get("team_id"))).size() == 0) {
			aaType = AAType.NoPart;
		}
		else {
			aaType = AAType.HavaPart;
		}
		categoryNames = categoryDao.getCategoryNames(1);
	}
	/**
	 * 初始化界面
	 */
	private void initView() {
		initData();
		spinnerTeamName = (Spinner) findViewById(R.id.consume_spin_team);
		spinnerTeamName.setAdapter(new ArrayAdapter<String>(ConsumeSeptActivity.this, 
				android.R.layout.simple_list_item_1, teamNames));
		
		spinnerConsumeMode = (Spinner) findViewById(R.id.consume_spin_mode);
		spinnerConsumeMode.setAdapter(new ArrayAdapter<String>(ConsumeSeptActivity.this, 
				android.R.layout.simple_list_item_1, modes));
		
		txtConsumeName = (EditText) findViewById(R.id.consume_txt_name);
		txtConsumeMoney = (EditText) findViewById(R.id.consume_txt_money);
		txtRemark = (EditText) findViewById(R.id.consume_txt_remark);
		txtTeamNum = (EditText) findViewById(R.id.consume_txt_people_num);
		
		viewPeopleChoice = (TextView) findViewById(R.id.consume_view_people);
		viewType = (TextView) findViewById(R.id.consume_view_type);
		viewDate = (TextView) findViewById(R.id.consume_view_date);
		viewConsumeMode = (TextView) findViewById(R.id.consume_view_mode);
		viewDate.setText(AApayUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
		
		btnPeopleSelect = (Button) findViewById(R.id.consume_btn_people_choice);
		btnTypeSelect = (Button) findViewById(R.id.consume_btn_type_choice);
		
		btnContinue = (Button) findViewById(R.id.consume_btn_continue);
		
		mergeDateTimePicker = getLayoutInflater().inflate(R.layout.date_time_merge, null,false);
		datePicker = (DatePicker) mergeDateTimePicker.findViewById(R.id.datePicker1);
		timePicker = (TimePicker) mergeDateTimePicker.findViewById(R.id.timePicker1);
		//timePicker.setCurrentHour(System.currentTimeMillis())
		timePicker.setIs24HourView(true);
	}

	/**
	 * 保存消费信息
	 */
	private boolean saveConsumption() {
		
		consumption.setTeamId(selectedTeamId);
		consumption.setTeamName(selectedTeamName);
		if(txtConsumeName.getText().toString().equals("")) {
			Toast.makeText(ConsumeSeptActivity.this, "请输入消费名称", Toast.LENGTH_SHORT).show();
			return false;
		}
		consumption.setName(txtConsumeName.getText().toString());
		if(txtConsumeMoney.getText().toString().equals("")) {
			Toast.makeText(ConsumeSeptActivity.this, "请输入消费金额！", Toast.LENGTH_SHORT).show();
			return false;
		}
		consumption.setMoney(Double.parseDouble(txtConsumeMoney.getText().toString()));
		
		consumption.setConsumptionMode(modes[spinnerConsumeMode.getSelectedItemPosition()]);
		consumption.setType(categoryNames[selectItem]);
		
		if(aaType == AAType.NoPart) {
			if(txtTeamNum.getText().toString().equals("") || Integer.parseInt(txtTeamNum.getText().toString())<=0) {
				Toast.makeText(ConsumeSeptActivity.this, "参与人员不正确！", Toast.LENGTH_SHORT).show();
				return false;
			}
			consumption.setParticipantNum(Integer.parseInt(txtTeamNum.getText().toString()));
		}
		else {
			if(selectedPartMaps == null) {
				Toast.makeText(ConsumeSeptActivity.this, "请至少选择一个参与者！", Toast.LENGTH_SHORT).show();
				return false;
			}
			else {
				consumption.setParticipantNum(selectedPartMaps.size());
			}
		}
		consumption.setParticipantList(selectedPartMaps);
		consumption.setRemark(txtRemark.getText().toString());
		consumption.setTime(getSelectedTime());
		consumption.setOperateTime(AApayUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		consumption.setPayOFF("否");
		
		//Toast.makeText(ConsumeSeptActivity.this, consumption.toString(), Toast.LENGTH_SHORT).show();
		return true;
	}
	
	/**
	 * 创建参与人员选择控件的Dialog
	 * @return
	 */
	private Dialog createPeopleSelectDialog() {
		mSlectedItems = new ArrayList<Integer>();
		mSelectedMap = new HashMap<Integer, Boolean>();
		final boolean [] checkedItems = new boolean[participantNames.length];
		for(int i = 0; i < checkedItems.length; i++) {
			checkedItems[i] = false;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.consume_pelple_select)
		.setMultiChoiceItems(participantNames, checkedItems, new OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if(which == 0) {
					for(int i =1; i < participantNames.length; i++) {
						mSelectedMap.put(Integer.valueOf(i), isChecked);
						checkedItems[i] = isChecked;
						((AlertDialog)dialog).getListView().setItemChecked(i, isChecked);
					}
				}
				else {
					mSelectedMap.put(Integer.valueOf(which), isChecked);
				}
				checkedItems[which] = isChecked;
				((AlertDialog)dialog).getListView().setItemChecked(which, isChecked);
				/*if(which == participantNames.length - 1) {
					if(isChecked) {
						for(int i = 0; i < participantNames.length - 1; i++) {
							if(!mSlectedItems.contains(i)) {
								mSlectedItems.add(i);
							}
						}
					}
					else {
						for(int i = 0; i < participantNames.length - 1; i++) {
							mSlectedItems.remove(Integer.valueOf(i));
						}
					}
				}
				else {
					if(isChecked) {
						mSlectedItems.add(which);
					}
					else if(mSlectedItems.contains(which)){
						mSlectedItems.remove(Integer.valueOf(which));
					}
				}*/
			}
		})
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				String text = "";
				//selectedPartItems = new ArrayList<Integer>();
				selectedPartMaps = new ArrayList<Map<String,String>>();
				for(Integer key : mSelectedMap.keySet()) {
					if(mSelectedMap.get(key)) {
						text += participantNames[key];
						text += ", ";
						Map<String,String> map = new HashMap<String,String>();
						map.put("part_id", participantList.get(key-1).get("part_id"));
						map.put("part_name", participantList.get(key-1).get("part_name"));
						selectedPartMaps.add(map);
					}
				}
				/*for (int i = 0; i < mSlectedItems.size(); i++) {
					text += participantNames[mSlectedItems.get(i)];
					text += ", ";
					//selectedPartItems.add(Integer.parseInt(participantList.get(mSlectedItems.get(i)).get("part_id")));
					Map<String,String> map = new HashMap<String,String>();
					map.put("part_id", participantList.get(mSlectedItems.get(i)).get("part_id"));
					map.put("part_name", participantList.get(mSlectedItems.get(i)).get("part_name"));
					selectedPartMaps.add(map);
				}*/
				/*Toast.makeText(ConsumeSeptActivity.this, 
						selectedPartMaps.toString(), Toast.LENGTH_SHORT).show();*/
				viewPeopleChoice.setText(text);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		return builder.create();
	}
	
	/**
	 * 创建消费类型选择对话框
	 * @return
	 */
	private Dialog createTypeSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.consume_type_select)
		.setSingleChoiceItems(categoryNames, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectItem = which;
			}
		})
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				Toast.makeText(ConsumeSeptActivity.this, 
						categoryNames[selectItem], Toast.LENGTH_SHORT).show();
				viewType.setText(categoryNames[selectItem]);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		return builder.create();
	}
	
	/**
	 * 创建Date和Time同时选择的控件
	 * @return
	 */
	private Dialog createDateTimeDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.date_time_select)
		.setView(mergeDateTimePicker)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getSelectedTime();
				((ViewGroup)mergeDateTimePicker.getParent()).removeView(mergeDateTimePicker);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((ViewGroup)mergeDateTimePicker.getParent()).removeView(mergeDateTimePicker);
			}
		});
		return builder.create();
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
		viewDate.setText(date);
		return date;
	}
	
	/**
	 * 获取选择的团队信息
	 * teamId,teamName,以及参与者
	 */
	private void getSelectedTeamInfo() {
		selectedTeamId = Long.parseLong(teamList.get(spinnerTeamName.getSelectedItemPosition()).get("team_id"));
		selectedTeamName = teamNames[spinnerTeamName.getSelectedItemPosition()];
		participantList = participantDao.getParticipantList(selectedTeamId);
		if(participantList.size() != 0) {
			participantNames = new String[participantList.size()+1];
			participantNames[0] = "全选";
			for(int i = 1; i <= participantList.size(); i++) {
				participantNames[i] = participantList.get(i-1).get("part_name");
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//if(consumption != null) {
		//	consumption = null;
		//}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Toast.makeText(this, consumption.toString(), Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dbHelper != null) {
			dbHelper.close();
		}
	}
}
