package com.bishe.aapay.activity;

import java.util.List;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.view.LvBudgetListAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

public class BudgetListActivity extends Activity {

	private ActionBar actionBar;
	private AApayDBHelper helper;
	private PersonalBudgetDAO personalBudgetDAO;
	
	private ListView lvBudgeList;
	private LvBudgetListAdapter lvBudgetListAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.budget_list_activity);
		setContentView(R.layout.activity_budget_list);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		helper = new AApayDBHelper(this);
		personalBudgetDAO = new PersonalBudgetDAO(helper);
		
		initView();
	}
	
	private void initView() {
		lvBudgeList = (ListView) findViewById(R.id.budget_lv_list);
		lvBudgetListAdapter = new LvBudgetListAdapter(this, personalBudgetDAO);
		lvBudgeList.setAdapter(lvBudgetListAdapter);
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
}
