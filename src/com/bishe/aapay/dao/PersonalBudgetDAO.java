package com.bishe.aapay.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.activity.PersonBudgetAddActivity;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.dto.PersonalBudget;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonalBudgetDAO {
	private SQLiteDatabase db;
	private ContentValues values;
	private AApayDBHelper helper;
	
	public PersonalBudgetDAO(AApayDBHelper helper) {
		this.helper = helper;
		this.db = this.helper.getWritableDatabase();
		this.values = new ContentValues();
	}
	
	
	public List<PersonalBudget> findAll() {
			List<PersonalBudget> budgets = new ArrayList<PersonalBudget>();

			Cursor cursor = db.rawQuery("select * from tb_budget",null);
			while(cursor.moveToNext()) {
				PersonalBudget budget = new PersonalBudget();
				budget.setId(cursor.getLong(cursor.getColumnIndex("_id")));
				budget.setBudgetType(cursor.getInt(cursor.getColumnIndex("budget_type")));
				budget.setBudgetMoney(cursor.getDouble(cursor.getColumnIndex("budget_money")));
				budget.setBudgetCategory(cursor.getString(cursor.getColumnIndex("budget_category")));
				budget.setBudgetTime(cursor.getString(cursor.getColumnIndex("budget_time")));
				budget.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
				budget.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
				budgets.add(budget);
			}
			cursor.close();
			return budgets;
	}
	
	/**
	 * 删除所有的数据
	 */
	public void deleteAll() {
		db.delete(AApayDBHelper.BUDGET_TABLE_NAME,null, null);
	}
	
	public void updateSequence(int size) {
		db.execSQL("update sqlite_sequence set seq = seq - ? where name = 'tb_budget'", new String[]{String.valueOf(size)});
	}
	/**
	 * 插入所有的数据
	 * @param categories
	 */
	public void batchInsert(List<PersonalBudget> personalBudgets) {
		if(personalBudgets == null)
			return;
		deleteAll();
		db.beginTransaction();
		try {
			for(PersonalBudget budget : personalBudgets) {
				setConetentValues(budget);
				this.values.put("_id", budget.getId());
				db.insert(AApayDBHelper.BUDGET_TABLE_NAME, null, this.values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			
		}
		finally {
			db.endTransaction();
		}
	}
	/**
	 * 插入
	 * @param budget
	 * @return
	 */
	public long insert(PersonalBudget budget) {
		setConetentValues(budget);
		return db.insert(AApayDBHelper.BUDGET_TABLE_NAME, null, values);
	}
	
	/**
	 * 获取最近添加的个人收支
	 * @return
	 */
	public PersonalBudget getRecentPersonalBudget() {
		PersonalBudget personalBudget = null;
		Cursor cursor = db.rawQuery("select * from tb_budget order by operate_time desc", null);
		if(cursor.moveToNext()) {
			personalBudget = new PersonalBudget();
			personalBudget.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			personalBudget.setBudgetType(cursor.getInt(cursor.getColumnIndex("budget_type")));
			personalBudget.setBudgetMoney(cursor.getDouble(cursor.getColumnIndex("budget_money")));
			personalBudget.setBudgetCategory(cursor.getString(cursor.getColumnIndex("budget_category")));
			personalBudget.setBudgetTime(cursor.getString(cursor.getColumnIndex("budget_time")));
			personalBudget.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
			personalBudget.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
		}
		return personalBudget;
	}
	
	/**
	 * 根据添加顺序获得所有Budget
	 * @return
	 */
	public List<PersonalBudget> getPersonalBudgetList() {
		List<PersonalBudget> budgetList = new ArrayList<PersonalBudget>();
		Cursor cursor = db.rawQuery("select * from tb_budget order by operate_time desc", null);
		while(cursor.moveToNext()) {
			PersonalBudget personalBudget = new PersonalBudget();
			personalBudget.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			personalBudget.setBudgetType(cursor.getInt(cursor.getColumnIndex("budget_type")));
			personalBudget.setBudgetMoney(cursor.getDouble(cursor.getColumnIndex("budget_money")));
			personalBudget.setBudgetCategory(cursor.getString(cursor.getColumnIndex("budget_category")));
			personalBudget.setBudgetTime(cursor.getString(cursor.getColumnIndex("budget_time")));
			personalBudget.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
			personalBudget.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			budgetList.add(personalBudget);
		}
		return budgetList;
	}
	
	public double getTotalMoneyByTimeInterval(String startTime,String endTime,int type) {
		double totalMoney = 0;
		Cursor cursor = db.rawQuery("select sum(budget_money) total_money from tb_budget " +
				"where budget_time > ? and budget_time < ? and budget_type = ? order by operate_time desc", 
				new String[]{startTime,endTime,String.valueOf(type)});
		if(cursor.moveToNext()) {
			totalMoney = cursor.getDouble(cursor.getColumnIndex("total_money"));
		}
		return totalMoney;
	}
	
	public List<Map<String, String>> getBudgetByType(int budgetType) {
		ArrayList<Map<String, String>> consuMaps = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select budget_category type , sum(budget_money) sept_money " +
				"from tb_budget  where budget_type = ? group by type;", new String[]{String.valueOf(budgetType)});
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("sept_money", String.valueOf(cursor.getDouble(cursor.getColumnIndex("sept_money"))));
			consuMaps.add(map);
		}
		cursor.close();
		return consuMaps;
	}
	
	public List<PersonalBudget> getListByTimeInterval(String startTime,String endTime) {
		List<PersonalBudget> budgets = new ArrayList<PersonalBudget>();
		Cursor cursor = db.rawQuery("select * from tb_budget " +
				"where budget_time > ? and budget_time < ?", 
				new String[]{startTime,endTime});
		while(cursor.moveToNext()) {
			PersonalBudget personalBudget = new PersonalBudget();
			personalBudget.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			personalBudget.setBudgetType(cursor.getInt(cursor.getColumnIndex("budget_type")));
			personalBudget.setBudgetMoney(cursor.getDouble(cursor.getColumnIndex("budget_money")));
			personalBudget.setBudgetCategory(cursor.getString(cursor.getColumnIndex("budget_category")));
			personalBudget.setBudgetTime(cursor.getString(cursor.getColumnIndex("budget_time")));
			personalBudget.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
			personalBudget.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			budgets.add(personalBudget);
		}
		return budgets;
	}
	public void setConetentValues(PersonalBudget budget) {
		values.clear();
		values.put("budget_type", budget.getBudgetType());
		values.put("budget_money", budget.getBudgetMoney());
		values.put("budget_category", budget.getBudgetCategory());
		values.put("budget_time", budget.getBudgetTime());
		values.put("operate_time", budget.getOperateTime());
		values.put("remark", budget.getRemark());
	}
}
