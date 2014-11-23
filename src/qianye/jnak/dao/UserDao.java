package qianye.jnak.dao;

import qianye.jnak.model.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {
	SQLiteDatabase db;
	DBHelper h;

	public UserDao(Context context) {
		h = new DBHelper(context);
		db = h.getWritableDatabase();

	}

	public Cursor queryAll() {

		return db.rawQuery("select * from user", null);
	}

	public void Add(User cus) {
		ContentValues values = new ContentValues();
		values.put("username", cus.getUsername());
		values.put("password", cus.getPassword());
		values.put("salt", cus.getSalt());
		values.put("nickname", cus.getNickname());
		db.insert("user", null, values);
	}

	public void delete(int id) {
		String[] bindArgs = new String[] { String.valueOf(id) };
		db.execSQL("delete from customer where _id=?", bindArgs);
	}

	public User get(String username) {
		User cus=new User();
		String[] bindArgs = new String[] { String.valueOf(username) };
		Cursor cur= db.rawQuery("select * from User where username=?", bindArgs);
		if(cur.moveToNext())
		{
			cus.setUsername(cur.getString(cur.getColumnIndex("username")));
			cus.setPassword(cur.getString(cur.getColumnIndex("password")));
			cus.setSalt(cur.getString(cur.getColumnIndex("salt")));
			cus.setNickname(cur.getString(cur.getColumnIndex("nickname")));
			cus.set_id(cur.getInt(cur.getColumnIndex("_id")));
		}
	return cus;	
	}

	public int update(User cus) {
		String[] bindArgs = new String[] { String.valueOf(cus.getUsername()) };
		ContentValues values = new ContentValues();
		values.put("password", cus.getPassword());
		values.put("salt", cus.getSalt());
		values.put("nickname", cus.getNickname());
		return db.update("User", values, "username=?", bindArgs);
	}

	/**
	 * 根据输入内容模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public Cursor query(String name) {
		return db.rawQuery("select * from User where username like '%" + name
				+ "%' limit 10", null);
	}
}
