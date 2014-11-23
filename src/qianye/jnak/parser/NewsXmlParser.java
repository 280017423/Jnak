package qianye.jnak.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import qianye.jnak.R;
import qianye.jnak.common.FileAccess;
import qianye.jnak.dao.ArticleDao;
import qianye.jnak.model.Article;

import android.content.Context;
import android.util.Xml;

/* **
 * ������������б�
 * 
 * @Description: ������������б?����ֻ�Ǹ�ʾ�����ز���ʵ�֡�
 * 
 * @File: NewsXmlParser.java
 * 
 * @Package com.image.indicator.parser
 * 
 * @Author Hanyonglu
 * 
 * @Date 2012-6-18 ����02:31:26
 * 
 * @Version V1.0
 */
public class NewsXmlParser {
	// �����б�
	private List<HashMap<String, Article>> newsList = null;

	ArticleDao dao;

	public NewsXmlParser() {
	}

	public NewsXmlParser(Context context) {
		dao = new ArticleDao(context);
	}

	// ����ͼƬ�ļ��ϣ��������ó��˹̶����أ���ȻҲ�ɶ�̬���ء�
	private int[] slideImages = { R.drawable.tb1, R.drawable.tb2, R.drawable.tb3 };

	// �������ӵļ���
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

	/**
	 * ��ȡXmlPullParser����
	 * 
	 * @param result
	 * @return
	 */
	private XmlPullParser getXmlPullParser(String result) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream inputStream = FileAccess.String2InputStream(result);

		try {
			parser.setInput(inputStream, "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return parser;
	}

	public int getNewsListCount(String result) {
		int count = -1;

		try {
			XmlPullParser parser = getXmlPullParser(result);
			int event = parser.getEventType();// �����һ���¼�

			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
						if ("count".equals(parser.getName())) {// �жϿ�ʼ��ǩԪ���Ƿ���count
							count = Integer.parseInt(parser.nextText());
						}

						break;
					case XmlPullParser.END_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
						// if("count".equals(parser.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���count
						// count = Integer.parseInt(parser.nextText());
						// }

						break;
				}

				event = parser.next();// ������һ��Ԫ�ز�������Ӧ�¼�
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// �޷���ֵ���򷵻�-1
		return count;
	}
}
