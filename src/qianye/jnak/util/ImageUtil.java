package qianye.jnak.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;

public class ImageUtil {
	private ImageUtil() {
	}

	public static Bitmap readBitMap(String url) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		return BitmapFactory.decodeFile(url, opt);
	}

	/**
	 * 保存bitmap到本地相册
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            标题
	 * @param bitmap
	 *            图片
	 * @return
	 */
	public static String addToTouchActiveAlbum(Context context, String title, Bitmap bitmap) {
		ContentValues values = new ContentValues();
		values.put(MediaColumns.TITLE, title);
		values.put(ImageColumns.DATE_TAKEN, System.currentTimeMillis());
		values.put(MediaColumns.MIME_TYPE, "image/jpeg");
		values.put(ImageColumns.DESCRIPTION, "abcd");
		ContentResolver contentResolver = context.getContentResolver();
		String stringUrl = null;
		Uri uri = null;
		try {
			uri = context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
			if (bitmap != null) {
				OutputStream imageOut = contentResolver.openOutputStream(uri);
				try {
					bitmap.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
				} finally {
					imageOut.close();
					long id = ContentUris.parseId(uri);
					// Wait until MINI_KIND thumbnail is generated.
					Bitmap miniThumb = Images.Thumbnails.getThumbnail(contentResolver, id, Images.Thumbnails.MINI_KIND,
							null);
					// This is for backward compatibility.
					StoreThumbnail(contentResolver, miniThumb, id, 50F, 50F, Images.Thumbnails.MICRO_KIND);
				}
			} else {
				contentResolver.delete(uri, null, null);
				uri = null;
			}
			stringUrl = uri.toString();
		} catch (Exception e) {
			if (uri != null) {
				contentResolver.delete(uri, null, null);
				uri = null;
			}
		}
		if (uri != null) {
			stringUrl = uri.toString();
		}
		return stringUrl;
	}

	private static Bitmap StoreThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {
		// create the matrix to scale it
		Matrix matrix = new Matrix();
		float scaleX = width / source.getWidth();
		float scaleY = height / source.getHeight();
		matrix.setScale(scaleX, scaleY);
		Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
		ContentValues values = new ContentValues(4);
		values.put(Images.Thumbnails.KIND, kind);
		values.put(Images.Thumbnails.IMAGE_ID, (int) id);
		values.put(Images.Thumbnails.HEIGHT, thumb.getHeight());
		values.put(Images.Thumbnails.WIDTH, thumb.getWidth());
		Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);
		try {
			OutputStream thumbOut = cr.openOutputStream(url);

			thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
			thumbOut.close();
			return thumb;
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		}
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		int width = drawable.getIntrinsicWidth();
		width = width > 0 ? width : 1;
		int height = drawable.getIntrinsicHeight();
		height = height > 0 ? height : 1;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
}
