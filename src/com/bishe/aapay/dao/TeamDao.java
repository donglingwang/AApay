package com.bishe.aapay.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.dto.Team;

import android.R.integer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.webkit.WebChromeClient.CustomViewCallback;

public class TeamDao  {
	private SQLiteDatabase db;
	private ContentValues values;
	private AApayDBHelper helper;
	public TeamDao(AApayDBHelper helper) {
		this.helper = helper;
		this.db = helper.getWritableDatabase();
		values = new ContentValues();
	}
	
	
	public List<Team> findAll() {
			List<Team> teams = new ArrayList<Team>();		
			Cursor cursor = db.rawQuery("select * from tb_team",null);
			while(cursor.moveToNext()) {
						Team team = new Team();
						team.setTeamId(cursor.getInt(cursor.getColumnIndex("_id")));
						team.setTeamName(cursor.getString(cursor.getColumnIndex("team_name")));
						team.setTeamNum(cursor.getInt(cursor.getColumnIndex("team_num")));
						team.setFoundDate(cursor.getString(cursor.getColumnIndex("found_date")));
						team.setConsumeTotalAmount(cursor.getDouble(cursor.getColumnIndex("consume_amount")));
						team.setHasPart(cursor.getString(cursor.getColumnIndex("has_part")));
						teams.add(team);
			}
			cursor.close();
			return teams;
	}
	
	/**
	 * 删除所有的数据
	 */
	public void deleteAll() {
		db.delete(AApayDBHelper.TEAM_TABLE_NAME,null, null);
	}
	
	public void updateSequence(int size) {
		db.execSQL("update sqlite_sequence set seq = seq - ? where name = 'tb_team'", new String[]{String.valueOf(size)});
	}
	/**
	 * 插入所有的数据
	 * @param categories
	 */
	public void batchInsert(List<Team> teams) {
		if(teams == null)
			return;
		deleteAll();
		db.beginTransaction();
		try {
			for(Team team : teams) {
				values.clear();
				this.values.put("_id", team.getTeamId());
				this.values.put("team_name", team.getTeamName());
				this.values.put("team_num", team.getTeamNum());
				this.values.put("found_date", team.getFoundDate());
				this.values.put("has_part", team.getHasPart());
				this.values.put("consume_amount", team.getConsumeTotalAmount());
				db.insert(AApayDBHelper.TEAM_TABLE_NAME, null, this.values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			
		}
		finally {
			db.endTransaction();
		}
	}
	/**
	 *  插入数据
	 * @param team
	 * @return
	 */
	public long insertTeam(Team team) {
		setContentValues(team);
		return db.insert(AApayDBHelper.TEAM_TABLE_NAME, null, this.values);
	}
	
	/**
	 * 获取所有团队的名称
	 * @return
	 */
	public String[] getTeamNames() {
		Cursor cursor = db.query(AApayDBHelper.TEAM_TABLE_NAME, new String[]{"_id","team_name"}, null, null, null, null, "found_date desc");
		String [] names = new String[cursor.getCount()];
		int index = 0;
		while (cursor.moveToNext()) {
			String temp = String.valueOf(cursor.getLong(cursor.getColumnIndex("_id")));
			temp += ",";
			temp +=	cursor.getString(cursor.getColumnIndex("team_name"));
			names[index] = temp;
			index++;
		}
		cursor.close();
		return names;
	}
	
	/**
	 * 获取TeamList
	 * @return
	 */
	public List<Map<String, String>> getTeamList() {
		ArrayList<Map<String, String>> teamList = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select * from tb_team order by found_date desc", null);
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("team_id", String.valueOf(cursor.getLong(cursor.getColumnIndex("_id"))));
			map.put("team_num", cursor.getString(cursor.getColumnIndex("team_num")));
			map.put("team_name", cursor.getString(cursor.getColumnIndex("team_name")));
			teamList.add(map);
		}
		cursor.close();
		return teamList;
	}
	/**
	 * 根据ID数组删除
	 * @param ids
	 */
	public void deleteByIds(ArrayList<Integer>  ids) {
		String args = TextUtils.join(", ", ids);
		db.execSQL(String.format("delete from tb_team where _id in (%s);",args));
	}
	/**
	 * 根据id获取Team
	 * @param id
	 * @return
	 */
	public Team geTeamById(String id) {
		Team team = null;
		Cursor cursor = db.query(AApayDBHelper.TEAM_TABLE_NAME, 
				new String[] {"team_name","team_num","found_date","consume_amount","has_part"}, 
				"_id = ?", new String[] {id}, null, null, null);
		if(cursor.moveToNext()) {
			team = new Team();
			team.setTeamName(cursor.getString(cursor.getColumnIndex("team_name")));
			team.setTeamNum(cursor.getInt(cursor.getColumnIndex("team_num")));
			team.setFoundDate(cursor.getString(cursor.getColumnIndex("found_date")));
			team.setConsumeTotalAmount(cursor.getDouble(cursor.getColumnIndex("consume_amount")));
			team.setHasPart(cursor.getString(cursor.getColumnIndex("has_part")));
		}
		cursor.close(); 
		return team;
	}
	
	public void updateTeamNum(String teamId,String teamNum) {
		values.clear();
		values.put("team_num", teamNum);
		db.update(AApayDBHelper.TEAM_TABLE_NAME, values, "_id=?", new String[]{teamId});
	}
	
	/**
	 * 设置ContentValues
	 * @param team
	 */
	private void setContentValues(Team team) {
		values.clear();
		this.values.put("team_name", team.getTeamName());
		this.values.put("team_num", team.getTeamNum());
		this.values.put("found_date", team.getFoundDate());
		this.values.put("has_part", team.getHasPart());
		this.values.put("consume_amount", team.getConsumeTotalAmount());
	}
}
