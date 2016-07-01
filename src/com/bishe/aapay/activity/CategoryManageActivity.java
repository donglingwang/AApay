package com.bishe.aapay.activity;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.CategoryDao;
import com.bishe.aapay.dto.Category;
import com.bishe.aapay.view.LvCategoryAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class CategoryManageActivity extends Activity {

	private ActionBar actionBar;
	private EditText txtCategoryEdit;
	private Button btnCategoryAdd;
	private ListView lvCategoryList;
	private Spinner spinnerCategotyType;
	private AApayDBHelper dbHelper;
	private CategoryDao categoryDao;
	private int selectedType = 1;
	private String [] types= {
			"AA消费","收入类别","支出类别"
	};
	private LvCategoryAdapter lvCategoryAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.category_manage_activity);
		setContentView(R.layout.activity_category_manage);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		dbHelper = new AApayDBHelper(this);
		categoryDao = new CategoryDao(dbHelper);
		
		lvCategoryAdapter = new LvCategoryAdapter(this, categoryDao,selectedType);
		initView();
		
		/*
		 * 选择不同的类别查看
		 */
		spinnerCategotyType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedType = position + 1;
				lvCategoryAdapter.refreshCategoryList(selectedType);
				lvCategoryAdapter.notifyDataSetChanged();
				txtCategoryEdit.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		/*
		 * 添加类别
		 */
		btnCategoryAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(txtCategoryEdit.getText().toString().equals("")) {
					Toast.makeText(CategoryManageActivity.this, "请输入数据", Toast.LENGTH_SHORT).show();
				}
				Category category = new Category(txtCategoryEdit.getText().toString());
				categoryDao.insertCategory(category);
				lvCategoryAdapter.refreshCategoryList(selectedType);
				lvCategoryAdapter.notifyDataSetChanged();
				txtCategoryEdit.setText("");
			}
		});
		
	}
	
	private void initView() {
		txtCategoryEdit = (EditText) findViewById(R.id.category_txt_edit);
		btnCategoryAdd = (Button) findViewById(R.id.category_btn_add);
		spinnerCategotyType = (Spinner) findViewById(R.id.category_spinner_type);
		spinnerCategotyType.setAdapter(new ArrayAdapter<String>(CategoryManageActivity.this, 
				android.R.layout.simple_list_item_1, types));
		lvCategoryList = (ListView) findViewById(R.id.category_lv_list);
		lvCategoryList.setAdapter(lvCategoryAdapter);
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
			dbHelper.close();
		}
	}
}
