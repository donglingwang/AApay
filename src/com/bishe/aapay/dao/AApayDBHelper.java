package com.bishe.aapay.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AApayDBHelper extends SQLiteOpenHelper{

	/*
	 *   数据库文件
	 */
	private static final String DB_FILE_NAME = "aapay.db";
	/*
	 *   数据库表名
	 */
	public static final String TEAM_TABLE_NAME = "tb_team";
	public static final String PARTICIPANT_TABLE_NAME = "tb_participant";
	public static final String CATEGORY_TABLE_NAME = "tb_category";
	public static final String CONSUMPTION_TABLE_NAME = "tb_consumtion";
	public static final String PAYMENT_TABLE_NAME = "tb_payment";
	public static final String BUDGET_TABLE_NAME = "tb_budget";
	/*
	 *  创建数据库的语句
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
		//this(context, name, null, 2);//添加tb_category表
		//this(context,name,null,3);//写错了表名
		//this(context,name,null,4);//新增tb_consumption和tb_payment表
		//this(context,name,null,5);//给tb_category添加初始化数据
		//this(context,name,null,6);//给tb_sonsumtion添加操作时间列
		//this(context,name,null,7);//给tb_category添加type字段
		//this(context,name,null,8);//给tb_category添加初始化数据
		//this(context,name,null,9);//新建tb_budget表
		this(context,name,null,10);//给tb_team 添加新字段has_part

	}
	public AApayDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * 在第一次安装app的时候调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TEAM_TABLE_SQL);
		db.execSQL(PARTICIPANT_TABLE_SQL);
		db.execSQL(CATEGORY_TABLE_SQL);
		db.execSQL(CONSUMPTION_TABLE_SQL);
		db.execSQL(PAYMENT_TABLE_SQL);
		db.execSQL(BUDGET_TABLE_SQL);
		//类别表添加初始数据
		db.execSQL("insert into tb_team(team_name,team_num,found_date,has_part,consume_amount) values ('示例','1','2015-05-20 05:20','是',0)");
		db.execSQL("insert into tb_participant (part_name,team_id,phone,balance,total_amount) values('自己',1,'18119324951',0,0)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('食品酒水',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('居家物业',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('行车交通',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('休闲娱乐',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('学习进修',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('购物',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('旅游',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('其他',0,1)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('衣服饰品',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('食品酒水',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('生活用品',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('物业',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('行车交通',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('通讯',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('学习进修',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('休闲娱乐',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('人情往来',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('保险',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('其他',0,2)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('工资收入',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('利息收入',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('奖金分红',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('投资收入',0,3)");
		db.execSQL("insert into tb_category(category_name,parent_id,type) values('其他',0,3)");
	}

	/**
	 * 当数据库升级或版本号发生变化的时候调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("alter table tb_team add column has_part varchar(20)");
	}

}
