package qianye.jnak.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.webkit.WebView;

public class FCommon {

	// 合作身份者编码
	public final static String partner = "netjxt";
	// 交易安全检验码，由数字和字母组成的32位字符串
	public final static String key = "az3exqfb05k0hzpi8tr10bwfwnhs1weo";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public final static String input_charset = "utf-8";
	// 签名方式 不需修改
	public final static String sign_type = "MD5";

	
    /*
     * 判断目录是否存在
     */
    public static boolean isDirExist(String filePath){
        File file = new File(filePath);
        if(!file.exists())
            return false;  //file.mkdir(); //如果不存在则创建
        else{
            return true;
        }
    }

	/*
	 * 获得SD卡根目录
	 * */
	public static String getSDPath(){
		  File sdDir = null;
		  boolean sdCardExist = Environment.getExternalStorageState()
		  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		  if (sdCardExist)
		  {
		  sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		  }
		  return sdDir.toString();
		   
		 }
	/*
	 * 判断是否连接wifi
	 * */
	public static Boolean NetWorkIsWifi(Context mContext) {
		boolean netStatus = false;
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi.isConnected()) {
			netStatus = true;

		}
		return netStatus;

	}

	/*
	 * 判断网络是否可用
	 */
	public static boolean NetworkStatusOK(Context mContext) {
		boolean netStatus = false;
		try {
			ConnectivityManager connectManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if (activeNetworkInfo.isAvailable()
						&& activeNetworkInfo.isConnected()) {
					netStatus = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netStatus;
	}

	// 将回车(\r)和空格(" ")换成HTML格式对应的换行和空格
	public static String EncodeHtml(String strInPut) {
		String Temp = strInPut;
		// Temp.Replace(" ", "&nbsp;");
		Temp = Temp.replace("<", "&lt;");
		Temp = Temp.replace(">", "&gt;");
		// Temp.Replace("\r", "<br>");
		// Temp.Replace("&","&amp;");
		Temp = Temp.replace(",", "，");
		Temp = Temp.replace("\"", "&quot;");
		return Temp;
	}

	// 将HTML格式的换行和空格换成中的对应字符("\r"、" ")
	public static String DecodeHtml(String strInPut) {
		String Temp = strInPut;

		// Temp.Replace("<br>", "\r");
		// Temp.Replace("&nbsp;", " ");
		Temp = Temp.replace("&lt;", "<");
		Temp = Temp.replace("&gt;", ">");
		// Temp.Replace("&amp;","&");
		Temp = Temp.replace("&quot;", "\"");
		return Temp;
	}

	/**
	 * 判断网络是否有效
	 * 
	 * @param url
	 * @return　int
	 * @throws Exception
	 */
	public static void LoadWebViewUrl(WebView view, String goUrl) {
		try {
			boolean b = checkNetWork_a();
			if (b) {
				view.loadUrl(goUrl);
			} else
				view.loadUrl("file:///android_asset/warring.htm");
		} catch (Exception ex) {
			view.loadUrl("file:///android_asset/warring.htm");
		}
	}

	public static void LoadWebViewUrl(Context context, WebView view,
			String goUrl) {
		try {
			boolean b = checkNetWork_b(context);
			if (b)
				view.loadUrl(goUrl);
			else
				view.loadUrl("file:///android_asset/warring.htm");
		} catch (Exception ex) {
			view.loadUrl("file:///android_asset/warring.htm");
		}
	}

	private static boolean checkNetWork_b(Context context) {
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mNetworkInfo = connectMgr.getActiveNetworkInfo();
		boolean bmob = mobNetInfo.isConnected();
		boolean bwifi = wifiNetInfo.isConnected();
		if (!bmob && !bwifi) {
			// Log.i(TAG, "unconnect");
			// unconnect network
			return false;
		} else {
			// connect network
			return true;
		}

	}

	private static boolean checkNetWork_a() {
		int testCode = -1;
		try {
			String s = getContent("http://3g.huyue.com.cn/apk/testjson.htm");
			JSONArray array = new JSONArray(s);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				testCode = Integer.parseInt(obj.getString("testcode"));
			}
		} catch (Exception ex) {
		}
		if (testCode == 1)
			return true;
		else
			return false;
	}

	/**
	 * 获得当前程序版本号
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo("qianye.jnak",
					0).versionCode;
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		}
		return verCode;
	}

	/**
	 * 获得当前程序版本名称
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo("qianye.jnak",
					0).versionName;
		} catch (NameNotFoundException ex) {
			ex.printStackTrace();
		}
		return verName;
	}

	/**
	 * 获取网址内容
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getContent(String url) throws Exception {
		StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		// 设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"), 8192);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decodeBASE64(String key) throws Exception {
		byte[] data = Base64.decode(key.getBytes(), Base64.DEFAULT);
		String str = String.valueOf(data);
		return str;
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encodeBASE64(String key) throws Exception {
		byte[] data = Base64.encode(key.getBytes(), Base64.DEFAULT);
		String str = String.valueOf(data);
		return str;// new String(Base64.encode(key.getBytes(),
					// Base64.DEFAULT),"UTF-8");
	}

	public static String getMD5(String val) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes());
			byte[] m = md5.digest();// 加密
			return getString(m);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public static String getString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}

		return sb.toString().replace("-", "");
	}

	public static String getSing() {
		String str = "input_charset=" + input_charset + "&partner=" + partner
				+ key;
		str = md5One(str);
		return str;
	}

	public static String getUrlParam(String userName) {
		String para = "";

		para = "user=" + userName + "&input_charset=" + input_charset
				+ "&sign_type=" + sign_type + "&partner=" + partner + "&ysign="
				+ getSing();
		return para;
	}

	public static String md5One(String s) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
		md.update(s.getBytes());
		return byteArrayToHexString(md.digest());
	}

	public static String md5Three(String clientId, String pwd, String timestamp) {
		clientId = clientId == null ? "" : clientId;
		pwd = pwd == null ? "" : pwd;
		timestamp = timestamp == null ? "" : timestamp;
		while (timestamp.length() < 10) {
			timestamp = "0" + timestamp;
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
		md.update(clientId.getBytes());
		md.update(new byte[7]);
		md.update(pwd.getBytes());
		md.update(timestamp.getBytes());

		return byteArrayToHexString(md.digest());
	}

	private static String[] HexCode = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HexCode[d1] + HexCode[d2];
	}

	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result + byteToHexString(b[i]);
		}
		return result;
	}

}
