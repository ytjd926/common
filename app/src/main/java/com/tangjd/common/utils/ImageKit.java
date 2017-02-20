package com.tangjd.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.io.ByteArrayOutputStream;

/**
 * 图片操作工具包
 * @author Chris
 */
public class ImageKit {
	/**
	 * Drawable转为Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
     * Bitmap转为Drawable
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
    	if(context == null || bitmap == null)
    		return null;
    	Resources res = context.getResources();
    	Drawable drawable = new BitmapDrawable(res, bitmap);
    	return drawable;
    }
    
    /**
     * Bitmap转为Drawable
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
    	if(bitmap == null)
    		return null;
    	Drawable drawable = new BitmapDrawable(bitmap);
    	return drawable;
    }
    
    /**
     * Byte[]转Bitmap<br />
     * 不压缩图片
     * @param data
     * @return
     */
    public static Bitmap createBitmap(byte[] data) {
    	return createBitmap(data, false);
    }
    
    /**
     * Byte[]转Bitmap
     * @param data		图片数据
     * @param compress	是否压缩
     * @return
     */
    public static Bitmap createBitmap(byte[] data, boolean compress) {
    	if(data == null || data.length == 0) return null;
    	if(!compress) {
    		return BitmapFactory.decodeByteArray(data, 0, data.length);
    	}
    	Bitmap bitmap = null;
    	//对图片进行压缩  
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  
          
        //获取这个图片的宽和高  
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        //计算缩放比  
        int be = (int)(options.outHeight / 800f);  
        if(be <= 0)
             be = 1;
        options.inSampleSize = be;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        return bitmap;
    }
    
    /**
     * Bitmap转Byte[]
     * @param bitmap
     * @return
     */
    public static byte[] compressBitmap(Bitmap bitmap) {
    	if(bitmap == null) {
    		return null;
    	}
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Byte[]转Drawable
     * @param data
     * @return
     */
    public static Drawable createDrawable(byte[] data) {
    	return bitmapToDrawable(createBitmap(data));
    }
    
    /**
     * Bitmap转Byte[]
     * @param //bitmap
     * @return
     */
    public static byte[] compressDrawable(Drawable drawable) {
    	if(drawable == null) {
    		return null;
    	}
    	Bitmap bitmap = drawableToBitmap(drawable);
    	return compressBitmap(bitmap);
    }

    /**
     * 放大缩小图片 
     * @param bitmap
     * @param wScale  图片缩放比例(宽)
     * @param hScale  图片缩放比例(高)
     * @return
     */
    public static Bitmap createBitmap(Bitmap bitmap,float wScale,float hScale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(wScale, hScale);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }

    /**
     * 获取view的截图
     * @param view
     * @return
     */
    public static Bitmap getViewBitmap(View view) {
    	if(view == null) {
    		return null;
    	}
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();
//		view.setDrawingCacheEnabled(false);
		return bitmap;
    }
    
    /**
	 * 旋转图片
	 * @param bitmap
	 * @param degree
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, float degree) {
		if(bitmap == null) return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}
}
