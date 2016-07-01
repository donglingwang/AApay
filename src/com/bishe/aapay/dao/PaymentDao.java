package com.bishe.aapay.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.dto.Participant;
import com.bishe.aapay.dto.Payment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class PaymentDao {
	private SQLiteDatabase db;
	private ContentValues values;
	private AApayDBHelper helper;
	
	public PaymentDao(AApayDBHelper helper) {
		this.helper = helper;
		this.db = this.helper.getWritableDatabase();
		this.values = new ContentValues();
	}
	
	public List<Payment> findAll() {
			List<Payment> payments = new ArrayList<Payment>();
			Cursor cursor = db.rawQuery("select * from tb_payment",null);
			while(cursor.moveToNext()) {
				Payment payment = new Payment();
				payment.setId(cursor.getLong(cursor.getColumnIndex("_id")));
				payment.setConsumptionId(cursor.getLong(cursor.getColumnIndex("consumption_id")));
				payment.setPartId(cursor.getLong(cursor.getColumnIndex("part_id")));
				payment.setAdvanceMoney(cursor.getDouble(cursor.getColumnIndex("advance_money")));
				payments.add(payment);
			}
			cursor.close();
		return payments;
	}
	
	/**
	 * 删除所有的数据
	 */
	public void deleteAll() {
		db.delete(AApayDBHelper.PAYMENT_TABLE_NAME,null, null);
	}
	
	public void updateSequence(int size) {
		db.execSQL("update sqlite_sequence set seq = seq - ? where name = 'tb_payment'", new String[]{String.valueOf(size)});
	}
	/**
	 * 插入所有的数据
	 * @param categories
	 */
	public void batchInsert(List<Payment> payments) {
		if(payments == null)
			return;
		deleteAll();
		db.beginTransaction();
		try {
			for(Payment payment : payments) {
				setContentValues(payment);
				this.values.put("_id", payment.getId());
				db.insert(AApayDBHelper.PAYMENT_TABLE_NAME, null, this.values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			
		}
		finally {
			db.endTransaction();
		}
	}
	/**
	 * 插入Paymet List
	 * @param payments
	 */
	public void insertList(List<Payment> payments) {
		db.beginTransaction();
		try {
			for(Payment payment : payments) {
				setContentValues(payment);
				db.insert(AApayDBHelper.PAYMENT_TABLE_NAME, null, this.values);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			
		}
		finally {
			db.endTransaction();
		}
	}
	
	/**
	 * 根据消费ID获取参与人员的垫付额
	 * @param consumptionId
	 * @return
	 */
	public List<Map<String, String>> getPayments(long consumptionId) {
		ArrayList<Map<String, String>> payments = 
				new ArrayList<Map<String,String>>();
		Cursor cursor = db.rawQuery("select part_id,part_name,advance_money from tb_payment pay,tb_participant part where pay.part_id = part._id and consumption_id = ?", 
				new String[]{String.valueOf(consumptionId)});
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("part_id", String.valueOf(cursor.getLong(cursor.getColumnIndex("part_id"))));
			map.put("part_name", cursor.getString(cursor.getColumnIndex("part_name")));
			map.put("advance_money", cursor.getString(cursor.getColumnIndex("advance_money")));
			payments.add(map);
		}
		cursor.close();
		return payments;
	}
	
	/**
	 * 根据part_id和consumption_id联合查询payment和consumption
	 * select a.*,b.advance_money from tb_consumtion a,tb_payment b where a._id = b.consumption_id and b.part_id = 41 and b.consumption_id in (15,16,17) 
	 */
	public List<Map<String, String>> getConsumptionBypartId(Long partId,ArrayList<Long> ids) {
		List<Map<String, String>> consumptionList = new ArrayList<Map<String,String>>();
		String args = TextUtils.join(", ", ids);
		System.out.println(args);
		Cursor cursor = db.rawQuery(
				String.format("select a.*,b.advance_money from tb_consumtion a,tb_payment b " +
						"where a._id = b.consumption_id " +
						"and b.part_id = ? " +
						"and b.consumption_id in (%s) ", args), 
				new String[]{String.valueOf(partId)});
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("consumption_name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("consumption_type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("consumption_money", String.valueOf(cursor.getDouble(cursor.getColumnIndex("money"))));
			map.put("consumption_time", cursor.getString(cursor.getColumnIndex("consumption_time")));
			map.put("advance_money", String.valueOf(cursor.getDouble(cursor.getColumnIndex("advance_money"))));
			map.put("part_num", String.valueOf(cursor.getInt(cursor.getColumnIndex("part_num"))));
			consumptionList.add(map);
		}
		return consumptionList;
	}
	/**
	 * 
	 * @param payment
	 */
	private void setContentValues(Payment payment) {
		this.values.clear();
		this.values.put("consumption_id", payment.getConsumptionId());
		this.values.put("part_id", payment.getPartId());
		this.values.put("advance_money", payment.getAdvanceMoney());
	}
}
