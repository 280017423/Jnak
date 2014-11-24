package qianye.jnak.dao;

import java.util.ArrayList;

import qianye.jnak.model.Article;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ArticleDao {
	SQLiteDatabase db;
	DBHelper h;

	public ArticleDao(Context context) {
		h = new DBHelper(context);
		db = h.getWritableDatabase();

	}

	public Cursor queryAll() {

		return db.rawQuery("select * from article", null);
	}

	public void Add(Article cus) {
		ContentValues values = new ContentValues();
		values.put("articleid", cus.getArticleid());
		values.put("channelid", cus.getChannelid());
		values.put("categoryid", cus.getCategoryid());
		values.put("title", cus.getTitle());
		values.put("zhaiyao", cus.getZhaiyao());
		values.put("content", cus.getContent());
		values.put("createon", cus.getCreateon());
		values.put("picurl", cus.getPicurl());
		values.put("videourl", cus.getVideourl());
		db.insert("article", null, values);
	}

	public void delete(int articleid) {
		String[] bindArgs = new String[] { String.valueOf(articleid) };
		db.execSQL("delete from Article where _articleid=?", bindArgs);
	}

	public Article get(int id) {
		Article cus = new Article();
		String[] bindArgs = new String[] { String.valueOf(id) };
		Cursor cur = db.rawQuery("select * from article where _id=?", bindArgs);
		if (cur.moveToNext()) {
			cus.setTitle(cur.getString(cur.getColumnIndex("title")));
			cus.setZhaiyao(cur.getString(cur.getColumnIndex("zhaiyao")));
			cus.setContent(cur.getString(cur.getColumnIndex("content")));
			cus.setCreateon(cur.getString(cur.getColumnIndex("createon")));
			cus.setPicurl(cur.getString(cur.getColumnIndex("picurl")));
			cus.setVideourl(cur.getString(cur.getColumnIndex("videourl")));
			cus.setArticleid(cur.getInt(cur.getColumnIndex("articleid")));
			cus.setChannelid(cur.getInt(cur.getColumnIndex("channelid")));
			cus.setCategoryid(cur.getInt(cur.getColumnIndex("categoryid")));
			cus.set_id(id);
		}
		return cus;
	}
	
	public Article getByArticleId(int articleid) {
		Article cus = new Article();
		String[] bindArgs = new String[] { String.valueOf(articleid) };
		Cursor cur = db.rawQuery("select * from article where articleid=?", bindArgs);
		if (cur.moveToNext()) {
			cus.setTitle(cur.getString(cur.getColumnIndex("title")));
			cus.setZhaiyao(cur.getString(cur.getColumnIndex("zhaiyao")));
			cus.setContent(cur.getString(cur.getColumnIndex("content")));
			cus.setCreateon(cur.getString(cur.getColumnIndex("createon")));
			cus.setPicurl(cur.getString(cur.getColumnIndex("picurl")));
			cus.setVideourl(cur.getString(cur.getColumnIndex("videourl")));
			cus.setArticleid(cur.getInt(cur.getColumnIndex("articleid")));
			cus.setChannelid(cur.getInt(cur.getColumnIndex("channelid")));
			cus.setCategoryid(cur.getInt(cur.getColumnIndex("categoryid")));
			cus.set_id(cur.getInt(cur.getColumnIndex("_id")));
		}else
		{
			Log.d("文章未查到", String.valueOf(articleid) );
		}
		return cus;
	}

	public int update(Article cus) {
		String[] bindArgs = new String[] { String.valueOf(cus.get_id()) };
		ContentValues values = new ContentValues();
		values.put("articleid", cus.getArticleid());
		values.put("channelid", cus.getChannelid());
		values.put("categoryid", cus.getCategoryid());
		values.put("title", cus.getTitle());
		values.put("zhaiyao", cus.getZhaiyao());
		values.put("content", cus.getContent());
		values.put("createon", cus.getCreateon());
		values.put("picurl", cus.getPicurl());
		values.put("videourl", cus.getVideourl());
		return db.update("article", values, "articleid=?", bindArgs);
	}

	// 获得数据库中最大的文章ID
	public int GetMaxArticleId() {
		int id=0;
		String strSql = "select max(articleid) as articleid from article";
		Cursor cur = db.rawQuery(strSql, null);
		if (cur.moveToNext()) {
			id= cur.getInt(cur.getColumnIndex("articleid"));
			cur.close();
		} 
			return id;
	}

	// 获得数据库中最小的文章ID
	public int GetMinArticleId() {
		int id=0;
		String strSql = "select min(articleid) as articleid from article";
		Cursor cur = db.rawQuery(strSql, null);
		if (cur.moveToNext()) {
			id= cur.getInt(cur.getColumnIndex("articleid"));
			cur.close();
		} 
			return id;
		
	}

	/**
	 * 根据输入内容模糊查询
	 * 
	 * @param name
	 * @return
	 */
	public Cursor query(String title) {
		return db.rawQuery("select * from Article where title like '%" + title
				+ "%' limit 10", null);
	}

	public int GetArticleAllCount(int categoryid) {
		int count = 0;
		String sql="select count(1) as cnt from Article";
		if(categoryid>0)
		{
			sql = "select count(1) as cnt from Article where categoryid="+categoryid;
		}
		Cursor cur = db.rawQuery(sql, null);
		if (cur.moveToNext()) {
			count= cur.getInt(cur.getColumnIndex("cnt"));
			cur.close();
		}
			return count;
	}
	public Cursor getAllItems1(int currentPage, int pageSize) {
		int firstResult = (currentPage - 1) * pageSize;
		int maxResult = currentPage * pageSize;
		String sql = "select * from Article limit ?,?";
		Cursor cur = db.rawQuery(
				sql,
				new String[] { String.valueOf(firstResult),
						String.valueOf(maxResult) });
	// 不要关闭数据库
		return cur;
	}
	public ArrayList<Article> getAllItems(int currentPage, int pageSize,int categoryid) {
		int firstResult = (currentPage - 1) * pageSize;
		int maxResult = currentPage * pageSize;
		String[] params=new String[]{String.valueOf(firstResult),String.valueOf(maxResult) };
		String sql = "select * from Article limit ?,?";
		if(categoryid>0)
		{
			params=new String[]{String.valueOf(categoryid),String.valueOf(firstResult),String.valueOf(maxResult) };
			sql = "select * from Article where categoryid=? limit ?,?";
		}
		Cursor cur = db.rawQuery(sql,params);
		ArrayList<Article> items = new ArrayList<Article>();
		while (cur.moveToNext()) {
			Article article =new Article();
			article.set_id(cur.getInt(cur.getColumnIndex("_id")));
			article.setTitle(cur.getString(cur.getColumnIndex("title")));
			article.setZhaiyao(cur.getString(cur.getColumnIndex("zhaiyao")));
			article.setContent(cur.getString(cur.getColumnIndex("content")));
			article.setArticleid(cur.getInt(cur.getColumnIndex("articleid")));
			article.setPicurl(cur.getString(cur.getColumnIndex("picurl")));
			article.setVideourl(cur.getString(cur.getColumnIndex("videourl")));
			items.add(article);
		}// 不要关闭数据库
		return items;
	}
	
	public ArrayList<Article> getAllItems(int pageSize,int channelid) {
		return getAllItems(1,pageSize,channelid);
	}
}
