package com.bishe.aapay.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AApayDBHelper extends SQLiteOpenHelper{

	/*
	 *   ���ݿ��ļ�
	 */
	private static final String DB_FILE_NAME = "aapay.db";
	/*
	 *   ���ݿ����
	 */
	public static final String TEAM_TABLE_NAME = "tb_team";
	public static final String PARTICIPANT_TABLE_NAME = "tb_participant";
	public static final String CATEGORY_TABLE_NAME = "tb_category";
	public static final String CONSUMPTION_TABLE_NAME = "tb_consumtion";
	public static final String PAYMENT_TABLE_NAME = "tb_payment";
	public static final String BUDGET_TABLE_NAME = "tb_budget";
	/*
	 *  �������ݿ�����
	 */
	private static final String TEAM_TABLE_SQL = "create table if not exists tb_team" +
			"(_id integer primary key autoincrement," +
			"team_name varchar(50) not null, " +
			"team_num integer," +
			"found_date varchar(20)," +
			"has_part varchar(20) ," +
			"consume_amount decimal(10,2) )";
	private static final String PARTICIPANT_TABLE_SQL = "create table if not exists tb_participant (" +
			"_id integer primary key autoincrement," +
			"part_name varchar(20) not null, " +
			"team_id varchar(20) not null," +
			"phone varchar(20) ," +
			"balance decimal(10,2)," +
			"total_amount decimal(10,2))";
	private static final String CATEGORY_TABLE_SQL ="create table if not exists tb_category(" +
			"_id integer primary key autoincrement," +
			"category_name varchar(20) not null, " +
			"parent_id integer," +
			"type integer)";
	
	private static final String CONSUMPTION_TABLE_SQL = "create table if not exists tb_consumtion(" +
			"_id integer primary key autoincrement," +
			"name varchar(50) not null," +
			"team_id integer," +
			"consumption_mode varchar(20)," +
			"money decimal(10,2)," +
			"type varchar(20)," +
			"consumption_time varchar(20)," +
			"part_num integer," +
			"remark varchar(200)," +
			"operate_time varchar(20)," +
			"pay_off varchar(20))";
	
	private static final String PAYMENT_TABLE_SQL = "create table if not exists tb_payment(" +
			"_id integer primary key autoincrement," +
			"consumption_id integer not null," +
			" part_id integer," +
			"advance_money decimal(10,2))";
	
	private static final String BUDGET_TABLE_SQL = "create table if not exists tb_budget (" +
			"_id integer primary key autoincrement, " +
			"budget_type integer, " +
			"budget_money decimal(10,2), " +
			"budget_category varchar(20)," +
			"budget_time varchar(20)," +
			"operate_time varchar(20), " +
			"remark varchar(200))";
	
	public AApayDBHelper(Context context) {
		this(context, DB_FILE_NAME);
	}
	public AApayDBHelper(Context context, String name) {
		//this(context, name, null, 1);
		//this(context, name, null, 2);//���tb_category��
		//this(context,name,null,3);//д���˱���
		//this(context,name,null,4);//����tb_consumption��tb_payment��
		//this(context,name,null,5);//��tb_category��ӳ�ʼ������
		//this(context,name,null,6);//��tb_sonsumtion��Ӳ���ʱ����
		//this(context,name,null,7);//��tb_category���type�ֶ�
		//this(context,name,null,8);//��tb_category��ӳ�ʼ������
		//this(context,name,null,9);//�½�tb_budget��
		this(context,name,null,10);//��tb_team ������ֶ�has_part

	}
	public AApayDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * �ڵ�һ�ΰ�װapp��ʱ�����
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TEAM_TABLE_SQL);
		db.execSQL(PARTICIPANT_TABLE_SQL);
		db.execSQL(CATEGORY_TABLE_SQL);
		db.execSQL(CONSUMPTION_TABLE_SQL);
		db.execSQL(PAYMENT_TABLE_SQL);
		db.execSQL(BUDGET_TABLE_SQL);
		//������ӳ�ʼ����
		db.execSQL("insert into tb_team(team_name,team_num,found_date,has_part,consume_amount) values ('ʾ��','1','2015-05-20 05:20','��',0)");
		db.execSQL("insert into tb_participant (part_name,team_id,phone,balance,total_amount) values('�Լ�',1,'18119324951',0,0)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('ʳƷ��ˮ',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('�Ӽ���ҵ',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('�г���ͨ',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('��������',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('ѧϰ����',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('����',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('����',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('����',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('�·���Ʒ',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('ʳƷ��ˮ',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('������Ʒ',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('��ҵ',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('�г���ͨ',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('ͨѶ',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('ѧϰ����',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('��������',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('��������',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('����',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('����',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('��������',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('��Ϣ����',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('����ֺ�',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('Ͷ������',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('����',0,3)");
	}

	/**
	 * �����ݿ�������汾�ŷ����仯��ʱ�����
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("alter table tb_team add column has_part varchar(20)");
	}

}
