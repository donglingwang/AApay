package com.bishe.aapay.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.dto.Participant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class ParticipantDao {

	private SQLiteDatabase db;
	private AApayDBHelper helper;
	private ContentValues values;
	public ParticipantDao(AApayDBHelper helper) {
		super();
		this.helper = helper;
		this.db = helper.getWritableDatabase();
		values = new ContentValues();
	}
	
	public List<Participant> findAll() {
			List<Participant> participants = new ArrayList<Participant>();
			Cursor cursor = db.rawQuery("select * from tb_participant",null);
			while(cursor.moveToNext()) {
				Participant participant = new Participant();
				participant.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				participant.setName(cursor.getString(cursor.getColumnIndex("part_name")));
				participant.setTeamId(cursor.getLong(cursor.getColumnIndex("team_id")));
				participant.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
				participant.setBalance(cursor.getDouble(cursor.getColumnIndex("balance")));
				participant.setTotalAmount(cursor.getDouble(cursor.getColumnIndex("total_amount")));
				participants.add(participant);
			}
			cursor.close();
			return participants;
	}
	
	/**
	 * 删除所有的数据
	 */
	public void deleteAll() {
		db.delete(AApayDBHelper.PARTICIPANT_TABLE_NAME,null, null);
	}
	
	public void updateSequence(int size) {
		db.execSQL("update sqlite_sequence set seq = seq - ? where name = 'tb_participant'", new String[]{String.valueOf(size)});
	}
	/**
	 * 插入所有的数据
	 * @param categories
	 */
	public void batchInsert(List<Participant> participants) {
		if(participants == null) 
			return;
		deleteAll();
		db.beginTransaction();
		try {
			for(Participant participant : participants) {
				setContentValues(participant);
				this.values.put("_id", participant.getId());
				db.insert(AApayDBHelper.PARTICIPANT_TABLE_NAME, null, this.values);
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
	 * @param participant
	 * @return
	 */
	public long insertPart(Participant participant) {
		setContentValues(participant);
		return db.insert(AApayDBHelper.PARTICIPANT_TABLE_NAME, null, values);
	}
	
	
	public void deleteByTeamIds(ArrayList<Integer> ids) {
		String args = TextUtils.join(", ", ids);
		db.execSQL(String.format("delete from tb_participant where team_id in (%s);",args));
	}
	/**
	 * 插入List数据
	 * @param participants
	 * @param teamId
	 */
	public void insertParticipantList(List<Participant> participants,long teamId) {
		db.beginTransaction();
		try {
			for(Participant participant : participants) {
				participant.setTeamId(teamId);
				insertPart(participant);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			
		}
		finally {
			db.endTransaction();
		}
	}
	
	/**
	 * 根据团队的Id获取参与人员的名字
	 * @param teamId
	 * @return
	 */
	public String [] getParticipantNamesByTeamId(long teamId) {
		Cursor cursor = db.rawQuery("select _id,part_name from tb_participant where team_id = ?", new String[]{String.valueOf(teamId)});
		String [] names = new String [cursor.getCount()];
		int index = 0;
		while(cursor.moveToNext()) {
			String temp = "";
			temp += cursor.getLong(cursor.getColumnIndex("_id"));
			temp += ",";
			temp += cursor.getString(cursor.getColumnIndex("part_name"));
			names[index] = temp;
			index++;
		}
		cursor.close();
		return names;
	}
	
	public long getPartId(long teamId, String partName) {
		long partId = 0;
		Cursor cursor = db.rawQuery("select _id from tb_participant where team_id = ? and part_name = ?", new String[] {String.valueOf(teamId),partName});
		if(cursor.moveToNext()) {
			partId = cursor.getLong(cursor.getColumnIndex("_id"));
		}
		return partId;
	}
	
	public String getPartName(long partId) {
		String partName = "";
		Cursor cursor = db.rawQuery("select part_name from tb_participant where _id = ?", new String[] {String.valueOf(partId)});
		if(cursor.moveToNext()) {
			partName = cursor.getString(cursor.getColumnIndex("part_name"));
		}
		return partName;
	}
	
	public String getPhone(long partId) {
		String phone = "";
		Cursor cursor = db.rawQuery("select phone from tb_participant where _id = ?", new String[] {String.valueOf(partId)});
		if(cursor.moveToNext()) {
			phone = cursor.getString(cursor.getColumnIndex("phone"));
		}
		return phone;
	}
	/**
	 * 根据Id获取参与人员的信息
	 * 并封装到list<map>中
	 * @param teamId
	 * @return
	 */
	public List<Map<String, String>> getParticipantList(long teamId) {
		ArrayList<Map<String, String>> participants = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select * from tb_participant where team_id = ?", new String[]{String.valueOf(teamId)});
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("part_id", String.valueOf(cursor.getLong(cursor.getColumnIndex("_id"))));
			map.put("part_name", cursor.getString(cursor.getColumnIndex("part_name")));
			map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
			participants.add(map);
		}
		cursor.close();
		return participants;
	}
	public List<Map<String, String>> getParticipantList() {
		ArrayList<Map<String, String>> participants = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select distinct part_name, phone  from tb_participant;", null);
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("part_name", cursor.getString(cursor.getColumnIndex("part_name")));
			map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
			participants.add(map);
		}
		cursor.close();
		return participants;
	}
	/**
	 * 删除
	 * @param partId
	 */
	public void deleteById(String partId) {
		db.delete(AApayDBHelper.PARTICIPANT_TABLE_NAME, "_id=?", new String [] {partId});
	}
	/**
	 * 设置ContentValues
	 * @param participant
	 */
	private void setContentValues(Participant participant) {
		values.clear();
		values.put("team_id", participant.getTeamId());
		values.put("part_name", participant.getName());
		values.put("phone", participant.getPhone());
		values.put("balance", participant.getBalance());
		values.put("total_amount", participant.getTotalAmount());
	}
}
