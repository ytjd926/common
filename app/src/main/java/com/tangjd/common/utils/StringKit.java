package com.tangjd.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理类
 * @author Chris
 */
public class StringKit {
	
	/**
	 * 邮箱
	 */
	private static final Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	
	/**
	 * 数字
	 */
	private static final Pattern numericPattern = Pattern.compile("^[0-9]+$");
	
	/**
	 * 匹配空格、换行回车
	 */
	private static final Pattern blankPattern = Pattern.compile("\\s*|\t|\r|\n");
	
	/**
	 * 匹配首尾空行、换行回车
	 */
	private static final Pattern blankPatternLR = Pattern.compile("^\\s*|\\s*$");
	
	/**
	 * 判断是否为空字符
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(String src) {
		return src == null || src.trim().length() == 0 || "null".equals(src);
	}
	
	/**
	 * 判断是否是空字符包括null, 长度, 只包含空
	 * @param
	 * @return
	 */
	public static boolean isNotEmpty(String src) {
		return !isEmpty(src);
	}
	
	/**
	 * 判断是否为邮件
	 * @param src
	 * @return
	 */
	public static boolean isEmail(String src) {
		if(isEmpty(src))
			return false;
		return emailer.matcher(src).matches();
	}
	
	/**
	 * 判断是否只包含数
	 * @param src
	 * @return
	 */
	public static boolean isNumeric(String src) {
		if(isEmpty(src))
			return false;
		return numericPattern.matcher(src).matches();
	}
	
    /**
     * 去除空格、回车换行、Tab
     * @param str
     * @return
     */
    public static String trim(String str) {
    	if(isEmpty(str)) return str;
    	Matcher m = blankPattern.matcher(str);
    	String after = m.replaceAll("");
    	return after;
    }
    
    /**
     * 去除字符串两端空格回车、换行Tab
     * @param str
     * @return
     */
    public static String trimLR(String str) {
    	if(isEmpty(str)) return str;
    	Matcher m = blankPatternLR.matcher(str);
    	String after = m.replaceAll("");
    	return after;
    }
    
    //**************************************************************
    //InputStream String 互转
    //**************************************************************
    /**
     * 字符串转为输入流
     * @param str
     * @return
     */
    public static InputStream string2Stream(String str) {
    	if(isEmpty(str)) return null;
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }
    
    /**
     * 输入流转为字符串
     * @param inStream
     * @return
     * @throws IOException
     */
    public static String stream2String(InputStream inStream) throws IOException {
    	return new String(stream2Bytes(inStream));
    }
    
    /**
     * 输入流转为字符数
     * @param inStream
     * @return
     * @throws IOException
     */
    public static byte[] stream2Bytes(InputStream inStream) throws IOException {
 		ByteArrayOutputStream outStream = null;
 		try {
 			outStream = new ByteArrayOutputStream();
 			byte[] buffer = new byte[1024];
 			int len = 0;
 			while ((len = inStream.read(buffer)) != -1) {
 				outStream.write(buffer, 0, len);
 			}
 			return outStream.toByteArray();
 		} finally {
 			try {
 				if(outStream != null) {
 					outStream.close();
 				}
 			}catch(Exception e) {
 			}
 		}
    }
}
