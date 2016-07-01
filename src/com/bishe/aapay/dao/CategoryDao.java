package com.bishe.aapay.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dto.Category;
import com.bishe.aapay.dto.Payment;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDao {
	private SQLiteDatabase db;
	private AApayDBHelper dbHelper;
	private ContentValues values;
	
	public CategoryDao(AApayDBHelper dbHelper) {
		this.dbHelper = dbHelper;
		db = this.dbHelper.getWritableDatabase();
		values = new ContentValues();
	}
	
	public List<Category> findAll() {
			List<Category> categories = new ArrayList<Category>();
			Cursor cursor = db.rawQuery("select * from tb_category",null);
			while(cursor.moveToNext()) {
					Category category = new Category();
					category.setId(cursor.getInt(cursor.getColumnIndex("_id")));
					category.setCategoryName(cursor.getString(cursor.getColumnIndex("category_name")));
					category.setPartentId(cursor.getInt(cursor.getColumnIndex("parent_id")));
					category.setType(cursor.getInt(cursor.getColumnIndex("type")));
					categories.add(category);
			}
			cursor.close();
			return categories;
	}
	
	public void updateSequence(int size) {
		db.execSQL("update sqlite_sequence set seq = seq - ? where name = 'tb_category'", new String[]{String.valueOf(size)});
	}
	/**
	 * 删除所有的数据
	 */
	public void deleteAll() {
		db.delete(AApayDBHelper.CATEGORY_TABLE_NAME,null, null);
	}
	/**
	 * 插入所有的数据
	 * @param categories
	 */
	public void batchInsert(List<Category> categories) {
		if(categories == null) {
			return;
		}
		deleteAll();
		db.beginTransaction();
		try {
			for(Category category : categories) {
				values.clear();
				values.put("_id", category.getId());
				values.put("category_name", category.getCategoryName());
				values.put("parent_id", category.getPartentId());
				values.put("type", category.getType());
				db.insert(AApayDBHelper.CATEGORY_TABLE_NAME, null, this.values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			
		}
		finally {
			db.endTransaction();
		}
	}
	/**
	 * 插入数据
	 * @param category
	 * @return
	 */
	public long insertCategory(Category category) {
		setContentValues(category);
		return db.insert(AApayDBHelper.CATEGORY_TABLE_NAME, null, values);
	}
	
	/**
	 *  获取类别的List，
	 *  TODO 以后在扩展的时候，category里面应该加一个区别是哪一个用户的字段
	 * @return
	 */
	public List<Map<String, String>> getCategoryList() {
		ArrayList<Map<String, String>> categoryList = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select * from tb_category",null);
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("category_id", String.valueOf(cursor.getLong(cursor.getColumnIndex("_id"))));
			map.put("category_name", cursor.getString(cursor.getColumnIndex("category_name")));
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			categoryList.add(map);
		}
		cursor.close();
		return categoryList;
	}
	
	/**
	 *  获取类别的List，
	 *  TODO 以后在扩展的时候，category里面应该加一个区别是哪一个用户的字段
	 * @return
	 */
	public List<Map<String, String>> getCategoryList(int type) {
		ArrayList<Map<String, String>> categoryList = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select * from tb_category where type = ?",new String[]{String.valueOf(type)});
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("category_id", String.valueOf(cursor.getLong(cursor.getColumnIndex("_id"))));
			map.put("category_name", cursor.getString(cursor.getColumnIndex("category_name")));
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			categoryList.add(map);
		}
		cursor.close();
		return categoryList;
	}
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteCategoryById(String id) {
		db.delete(AApayDBHelper.CATEGORY_TABLE_NAME, "_id = ?", new String []{id});
	}
	
	/*
	 * 获取category的名字数组
	 */
	public String[] getCategoryNames(int type) {
		Cursor cursor = db.query(AApayDBHelper.CATEGORY_TABLE_NAME, new String[]{"_id","category_name"}, "type = ?", new String[]{String.valueOf(type)}, null, null, null);
		String [] names = new String[cursor.getCount()];
		int index = 0;
		while (cursor.moveToNext()) {
			//String temp = String.valueOf(cursor.getLong(cursor.getColumnIndex("_id")));
			//temp += ",";
			//temp +=	cursor.getString(cursor.getColumnIndex("category_name"));
			//names[index] = temp;
			names[index] = cursor.getString(cursor.getColumnIndex("category_name"));
			index++;
		}
		cursor.close();
		return names;
	}
	private void setContentValues(Category category) {
		//values.put("_id", category.getId());
		values.clear();
		values.put("category_name", category.getCategoryName());
		values.put("parent_id", category.getPartentId());
		values.put("type", category.getType());
	}
}
