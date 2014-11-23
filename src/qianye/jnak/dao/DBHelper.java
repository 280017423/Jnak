package qianye.jnak.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	String sql="CREATE TABLE [article] ([_id] integer PRIMARY KEY AUTOINCREMENT,  [channelid] integer, [categoryid] integer,   [articleid] integer, [title] varchar(100), [zhaiyao] varchar(255), [content] TEXT,[createon] DATETIME,[picurl] varchar(255),[videourl] varchar(255));";
	String sql2="CREATE TABLE [customer] ([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [phone] varchar(20), [mobile] varchar(20), [email] varchar(30), [company] varchar(255), [address] varchar(255), [name] VARCHAR(30), [sid] INTEGER,[gid] INTEGER,[username] varchar(255),[post] varchar(255),[createon] DATETIME);";
	String sql3="CREATE TABLE [user] ([_id] INTEGER PRIMARY KEY AUTOINCREMENT, [username] varchar(100), [password] varchar(100), [salt] varchar(20), [nickname] varchar(100));";
	public DBHelper(Context context) {
		super(context, "jnakdb.db", null, 5);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
		db.execSQL(sql2);
		db.execSQL(sql3);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion>oldVersion)
		{
//			String dSql1="alter table [customer] add username varchar(255)";
//			String dSql2="alter table [customer] add post varchar(255)";
//			String dSql3="alter table [customer] add createon DATETIME";
//			db.execSQL(dSql1);
//			db.execSQL(dSql2);
//			db.execSQL(dSql3);
			//onCreate(db);
		}
	}

}
