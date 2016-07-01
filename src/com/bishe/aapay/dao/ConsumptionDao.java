package com.bishe.aapay.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dto.Category;
import com.bishe.aapay.dto.Consumption;

import android.R.bool;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Contacts.Intents.Insert;
import android.text.TextUtils;
import android.webkit.WebChromeClient.CustomViewCallback;

public class ConsumptionDao {
	private SQLiteDatabase db;
	private ContentValues values;
	private AApayDBHelper helper;
	
	public ConsumptionDao(AApayDBHelper helper) {
		this.helper = helper;
		this.db = helper.getWritableDatabase();
		this.values = new ContentValues();
	}
	
	public List<Consumption> findAll() {
			List<Consumption> consumptions = new ArrayList<Consumption>();
			Cursor cursor = db.rawQuery("select * from tb_consumtion",null);
			while(cursor.moveToNext()) {
				Consumption consumption = new Consumption();
				consumption.setId(cursor.getLong(cursor.getColumnIndex("_id")));
				consumption.setName(cursor.getString(cursor.getColumnIndex("name")));
				consumption.setTeamId(cursor.getLong(cursor.getColumnIndex("team_id")));
				consumption.setConsumptionMode(cursor.getString(cursor.getColumnIndex("consumption_mode")));
				consumption.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
				consumption.setType(cursor.getString(cursor.getColumnIndex("type")));
				consumption.setTime(cursor.getString(cursor.getColumnIndex("consumption_time")));
				consumption.setParticipantNum(cursor.getInt(cursor.getColumnIndex("part_num")));
				consumption.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
				consumption.setPayOFF(cursor.getString(cursor.getColumnIndex("pay_off")));
				consumption.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
				consumptions.add(consumption);
			}
			cursor.close();
		return consumptions;
	}
	
	/**
	 * 删除所有的数据
	 */
	public void deleteAll() {
		db.delete(AApayDBHelper.CONSUMPTION_TABLE_NAME,null, null);
	}
	/**
	 * 插入所有的数据
	 * @param categories
	 */
	public void batchInsert(List<Consumption> consumptions) {
		if(consumptions == null) {
			return;
		}
		deleteAll();
		db.beginTransaction();
		try {
			for(Consumption consumption : consumptions) {
				setContentValues(consumption);
				this.values.put("_id", consumption.getId());
				db.insert(AApayDBHelper.CONSUMPTION_TABLE_NAME, null, this.values);
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
	 * @param consumption
	 * @return
	 */
	public long insert(Consumption consumption) {
		setContentValues(consumption);
		return this.db.insert(AApayDBHelper.CONSUMPTION_TABLE_NAME, null, this.values);
	}
	
	
	public void update(Consumption consumption) {
		setContentValues(consumption);
		db.update(AApayDBHelper.CONSUMPTION_TABLE_NAME, values, "_id = ?", new String[] {String.valueOf(consumption.getId())});
	}
	public List<Consumption> getConsumptionByIds(List<Long> ids) {
		List<Consumption> consumptions = new ArrayList<Consumption>();
		String args = TextUtils.join(",", ids);
		Cursor cursor = db.rawQuery(String.format("select * from tb_consumtion where _id in (%s)", args), null);
		while(cursor.moveToNext()) {
			Consumption consumption = new Consumption();
			consumption.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			consumption.setTeamId(cursor.getLong(cursor.getColumnIndex("team_id")));
			consumption.setName(cursor.getString(cursor.getColumnIndex("name")));
			consumption.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
			consumption.setType(cursor.getString(cursor.getColumnIndex("type")));
			consumption.setParticipantNum(cursor.getInt(cursor.getColumnIndex("part_num")));
			consumption.setTime(cursor.getString(cursor.getColumnIndex("consumption_time")));
			consumption.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			consumption.setPayOFF(cursor.getString(cursor.getColumnIndex("pay_off")));
			consumption.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
			consumptions.add(consumption);
		}
		cursor.close();
		return consumptions;
	}
	/**
	 * 获取团队的消费信息
	 * @param teamId
	 * @return
	 */
	public List<Consumption> getConsumptionsByTeamId(long teamId) {
		List<Consumption> consumptions = new ArrayList<Consumption>();
		Cursor cursor = db.rawQuery("select * from tb_consumtion where team_id = ? order by consumption_time DESC", new String[]{String.valueOf(teamId)});
		while(cursor.moveToNext()) {
			Consumption consumption = new Consumption();
			consumption.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			consumption.setTeamId(teamId);
			consumption.setName(cursor.getString(cursor.getColumnIndex("name")));
			consumption.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
			consumption.setType(cursor.getString(cursor.getColumnIndex("type")));
			consumption.setParticipantNum(cursor.getInt(cursor.getColumnIndex("part_num")));
			consumption.setTime(cursor.getString(cursor.getColumnIndex("consumption_time")));
			consumption.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			consumption.setPayOFF(cursor.getString(cursor.getColumnIndex("pay_off")));
			consumption.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
			consumptions.add(consumption);
		}
		cursor.close();
		return consumptions;
	}
	
	public void updateSequence(int size) {
		db.execSQL("update sqlite_sequence set seq = seq - ? where name = 'tb_consumtion'", new String[]{String.valueOf(size)});
	}
	
	public List<Consumption> getConsumptions(long teamId,String startTime, String endTime) {
		List<Consumption> consumptions = new ArrayList<Consumption>();
		Cursor cursor = db.rawQuery("select * from tb_consumtion where team_id = ? and consumption_time > ? and consumption_time < ? " +
				"order by consumption_time DESC", new String[]{String.valueOf(teamId),startTime,endTime});
		while(cursor.moveToNext()) {
			Consumption consumption = new Consumption();
			consumption.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			consumption.setTeamId(teamId);
			consumption.setName(cursor.getString(cursor.getColumnIndex("name")));
			consumption.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
			consumption.setType(cursor.getString(cursor.getColumnIndex("type")));
			consumption.setParticipantNum(cursor.getInt(cursor.getColumnIndex("part_num")));
			consumption.setTime(cursor.getString(cursor.getColumnIndex("consumption_time")));
			consumption.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
			consumption.setPayOFF(cursor.getString(cursor.getColumnIndex("pay_off")));
			consumption.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
			consumptions.add(consumption);
		}
		cursor.close();
		return consumptions;
	} 
	public double getMoneyByIds(List<Long> ids) {
		String args = TextUtils.join(",", ids);
		double totalMoney = 0;
		Cursor cursor = db.rawQuery(String.format("select sum(money) total_money from tb_consumtion where _id in (%s)", args), null);
		if(cursor.moveToNext()) {
			totalMoney = cursor.getDouble(cursor.getColumnIndex("total_money"));
		}
		return totalMoney;
	}
	
	public double getTotalMoney(long teamId) {
		double totalMoney = 0;
		Cursor cursor = db.rawQuery("select sum(money) total_money from tb_consumtion where team_id = ?", new String[]{String.valueOf(teamId)});
		if(cursor.moveToNext()) {
			totalMoney = cursor.getDouble(cursor.getColumnIndex("total_money"));
		}
		return totalMoney;
	}
	
	public double getSettleMoney(long teamId, String payOff) {
		double settleMoney = 0;
		Cursor cursor = db.rawQuery("select sum(money) total_money from tb_consumtion where team_id = ? and pay_off = ?",
				new String[]{String.valueOf(teamId),payOff});
		if(cursor.moveToNext()) {
			settleMoney = cursor.getDouble(cursor.getColumnIndex("total_money"));
		}
		return settleMoney;
	}
	
	public double getTotalMoney(long teamId, String startTime,String endTime) {
		double totalMoney = 0;
		Cursor cursor = db.rawQuery("select sum(money) total_money from tb_consumtion where team_id = ? and consumption_time > ? and consumption_time < ?", 
				new String[]{String.valueOf(teamId),startTime,endTime});
		if(cursor.moveToNext()) {
			totalMoney = cursor.getDouble(cursor.getColumnIndex("total_money"));
		}
		return totalMoney;
	}
	
	public double getSettleMoney(long teamId, String startTime, String endTime, String payOff) {
		double settleMoney = 0;
		Cursor cursor = db.rawQuery("select sum(money) total_money from tb_consumtion where " +
				"team_id = ? and consumption_time > ? and consumption_time < ? and pay_off = ?", new String[]{String.valueOf(teamId),startTime,endTime,payOff});
		if(cursor.moveToNext()) {
			settleMoney = cursor.getDouble(cursor.getColumnIndex("total_money"));
		}
		return settleMoney;
	}
	/**
	 * 根据Ids更新pay off
	 * @param ids
	 */
	public void updatePayOffByIds(List<Long> ids) {
		String args = TextUtils.join(",", ids);
		db.execSQL(String.format("update tb_consumtion set pay_off = '是' where _id in (%s)",args));
	}
	
	public void updatePayOffById(long id) {
		db.execSQL("update tb_consumtion set pay_off = '是' where _id = ?",new String[]{String.valueOf(id)});
	}
	
	public List<Map<String, String>> getConsumptionByType() {
		ArrayList<Map<String, String>> consuMaps = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select type , sum(money) sept_money from tb_consumtion group by type", null);
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("sept_money", String.valueOf(cursor.getDouble(cursor.getColumnIndex("sept_money"))));
			consuMaps.add(map);
		}
		cursor.close();
		return consuMaps;
	}
	/**
	 * 获取团队的消费信息
	 * @param teamId
	 * @return
	 */
	public List<Consumption> getConsumptionsByTeamId(long teamId,String payOff) {
		List<Consumption> consumptions = new ArrayList<Consumption>();
		Cursor cursor = db.rawQuery("select * from tb_consumtion where team_id = ? and pay_off = ? order by consumption_time DESC", new String[]{String.valueOf(teamId),payOff});
		while(cursor.moveToNext()) {
			Consumption consumption = new Consumption();
			consumption.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			consumption.setTeamId(teamId);
			consumption.setName(cursor.getString(cursor.getColumnIndex("name")));
			consumption.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
			consumption.setType(cursor.getString(cursor.getColumnIndex("type")));
			consumption.setParticipantNum(cursor.getInt(cursor.getColumnIndex("part_num")));
			consumption.setTime(cursor.getString(cursor.getColumnIndex("consumption_time")));
			consumption.setPayOFF(cursor.getString(cursor.getColumnIndex("pay_off")));
			consumption.setOperateTime(cursor.getString(cursor.getColumnIndex("operate_time")));
			consumptions.add(consumption);
		}
		cursor.close();
		return consumptions;
	}
	/**
	 * 
	 * @param consumption
	 */
	private void setContentValues(Consumption consumption) {
		this.values.clear();
		this.values.put("name", consumption.getName());
		this.values.put("team_id", consumption.getTeamId());
		this.values.put("consumption_mode", consumption.getConsumptionMode());
		this.values.put("money", consumption.getMoney());
		this.values.put("type", consumption.getType());
		this.values.put("consumption_time", consumption.getTime());
		this.values.put("part_num", consumption.getParticipantNum());
		this.values.put("remark", consumption.getRemark());
		this.values.put("operate_time", consumption.getOperateTime());
		this.values.put("pay_off", consumption.getPayOFF());
	}
}
