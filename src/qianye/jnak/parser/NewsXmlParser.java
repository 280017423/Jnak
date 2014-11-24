package qianye.jnak.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import qianye.jnak.R;
import qianye.jnak.common.FileAccess;
import qianye.jnak.dao.ArticleDao;
import qianye.jnak.model.Article;
import android.content.Context;
import android.util.Xml;

public class NewsXmlParser {
	ArticleDao dao;

	public NewsXmlParser(Context context) {
		dao = new ArticleDao(context);
	}

	private int[] slideImages = { R.drawable.tb1, R.drawable.tb2, R.drawable.tb3 };

	private String[] slideUrls = {
			"http://mobile.csdn.net/a/20120616/2806676.html",
			"http://cloud.csdn.net/a/20120614/2806646.html",
			"http://mobile.csdn.net/a/20120613/2806603.html" };

	public int[] getSlideImages() {
		return slideImages;
	}

	public String[] getSlideUrls() {
		return slideUrls;
	}

	public ArrayList<Article> GetTopicNews(int channelid, int count) {
		return dao.getAllItems(count, channelid);
	}

	private XmlPullParser getXmlPullParser(String result) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream inputStream = FileAccess.String2InputStream(result);

		try {
			parser.setInput(inputStream, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parser;
	}

	public int getNewsListCount(String result) {
		int count = -1;
		try {
			XmlPullParser parser = getXmlPullParser(result);
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if ("count".equals(parser.getName())) {
							count = Integer.parseInt(parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
}
