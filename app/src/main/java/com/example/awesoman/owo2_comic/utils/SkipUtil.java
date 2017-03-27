package com.example.awesoman.owo2_comic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Intent 跳转  工具类
 */
public class SkipUtil {
	/**
	 * 封装Intent跳转
	 * 
	 * @param clazz
	 *            要跳向的界面的class
	 * @param isCloseSelf
	 *            是否关闭本界面
	 */
	public static void skip(Context context,Class<?> clazz, boolean isCloseSelf) {
		Intent intent = new Intent(context, clazz);
		skip(context,intent, isCloseSelf);
	}
	
	/**
	 * 封装Intent跳转
	 * 
	 * @param intent
	 * @param isCloseSelf
	 *            是否关闭本界面
	 */
	public static void skip(Context context,Intent intent, boolean isCloseSelf) {
		context.startActivity(intent);
		if (isCloseSelf) {
			((Activity) context).finish();
		}
	}
}
