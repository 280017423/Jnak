/**
 * @Filename ImageCallback.java
 * @Package com.zsq.musiclibrary.listener
 * @Description TODO
 * @version 1.0
 * @author admin012 - 2014 Cindigo.All Rights Reserved.
 **/
package qianye.jnak.listener;

import android.graphics.Bitmap;

public interface ImageLoadListener {
	public void imageLoaded(Bitmap bitmap, String imageUrl);

}