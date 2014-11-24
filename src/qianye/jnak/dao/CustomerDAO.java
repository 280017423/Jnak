package qianye.jnak.dao;

import java.util.ArrayList;
import qianye.jnak.model.Customer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CustomerDAO {
	SQLiteDatabase db;
	DBHelper h;

	public CustomerDAO(Context context) {
		h = new DBHelper(context);
		db = h.getWritableDatabase();

	}

	public Cursor queryAll() {

		return db.rawQuery("select * from customer", null);
	}

	public void Add(Customer cus) {
		ContentValues values = new ContentValues();
		values.put("phone", cus.getPhone());
		values.put("mobile", cus.getMobile());
		values.put("email", cus.getEmail());
		values.put("company", cus.getCompany());
		values.put("name", cus.getName());
		values.put("address", cus.getAddress());
		values.put("sid", cus.getSid());
		values.put("gid", cus.getGid());
		values.put("username", cus.getUsername());
		values.put("post", cus.getPost());
		values.put("createon", cus.getCreateon());
		db.insert("customer", null, values);
	}

	public void delete(long id) {
		String[] bindArgs = new String[] { String.valueOf(id) };
		db.execSQL("delete from customer where _id=?", bindArgs);
	}

	public Customer get(long id) {
		Customer cus = new Customer();
		if (id > 0) {
			String[] bindArgs = new String[] { String.valueOf(id) };
			Cursor cur = db.rawQuery("select * from customer where _id=?",
					bindArgs);
			if (cur.moveToNext()) {
				cus.setUsername(cur.getString(cur.getColumnIndex("username")));
				cus.setPhone(cur.getString(cur.getColumnIndex("phone")));
				cus.setMobile(cur.getString(cur.getColumnIndex("mobile")));
				cus.setEmail(cur.getString(cur.getColumnIndex("email")));
				cus.setCompany(cur.getString(cur.getColumnIndex("company")));
				cus.setName(cur.getString(cur.getColumnIndex("name")));
				cus.setAddress(cur.getString(cur.getColumnIndex("address")));
				cus.setCreateon(cur.getString(cur.getColumnIndex("createon")));
				cus.setPost(cur.getString(cur.getColumnIndex("post")));
				cus.setSid(cur.getInt(cur.getColumnIndex("sid")));
				cus.setGid(cur.getInt(cur.getColumnIndex("gid")));
				cus.set_id(id);
			}
		}
		return cus;
	}

	public int update(Customer cus) {
		String[] bindArgs = new String[] { String.valueOf(cus.get_id()) };
		ContentValues values = new ContentValues();
		values.put("phone", cus.getPhone());
		values.put("mobile", cus.getMobile());
		values.put("email", cus.getEmail());
		values.put("company", cus.getCompany());
		values.put("name", cus.getName());
		values.put("address", cus.getAddress());
		values.put("sid", cus.getSid());
		values.put("gid", cus.getGid());
		values.put("username", cus.getUsername());
		values.put("post", cus.getPost());
		values.put("createon", cus.getCreateon());
		return db.update("customer", values, "_id=?", bindArgs);
	}

	/**
	 * 根据输入内容模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public Cursor query(String name) {
		return db.rawQuery("select * from customer where name like '%" + name
				+ "%' limit 10", null);
	}

	public int GetCustomerAllCount(String userName) {
		int count = 0;
		Cursor cur = db.rawQuery("select count(1) as cnt from customer where username='"+userName+"'", null);
		if (cur.moveToNext()) {
			count = cur.getInt(cur.getColumnIndex("cnt"));
			cur.close();
		}

		return count;
	}

	public ArrayList<Customer> getAllItems(String userName,int currentPage, int pageSize) {
		int firstResult = (currentPage - 1) * pageSize;
		int maxResult = currentPage * pageSize;
		String sql = "select * from Customer where username=? limit ?,?";
		Cursor cur = db.rawQuery(
				sql,
				new String[] { String.valueOf(userName),String.valueOf(firstResult),
						String.valueOf(maxResult) });
		ArrayList<Customer> items = new ArrayList<Customer>();
		while (cur.moveToNext()) {
			Customer customer = new Customer();
			customer.set_id(cur.getInt(cur.getColumnIndex("_id")));
			customer.setName(cur.getString(cur.getColumnIndex("name")));
			customer.setMobile(cur.getString(cur.getColumnIndex("mobile")));
			customer.setEmail(cur.getString(cur.getColumnIndex("email")));
			customer.setCompany(cur.getString(cur.getColumnIndex("company")));
			customer.setAddress(cur.getString(cur.getColumnIndex("address")));
			customer.setUsername(cur.getString(cur.getColumnIndex("username")));
			customer.setPost(cur.getString(cur.getColumnIndex("post")));
			customer.setSid(cur.getInt(cur.getColumnIndex("sid")));
			customer.setGid(cur.getInt(cur.getColumnIndex("gid")));
			items.add(customer);
		}// 不要关闭数据库
		return items;
	}
}
