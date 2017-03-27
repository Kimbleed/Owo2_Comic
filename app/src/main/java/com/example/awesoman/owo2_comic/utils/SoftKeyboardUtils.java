package com.example.awesoman.owo2_comic.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SoftKeyboardUtils {

	/**
	 * 强制弹出软键盘
	 */
	public static void showInput(final Context context, final EditText editText) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager inputManager = (InputMethodManager) editText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText,
						InputMethodManager.SHOW_FORCED);
			}
		}, 5);
	}

	/**
	 * 强制隐藏软键盘
	 */
	// 不起作用
	public static void hideInput(final Context context, final EditText editText) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				// InputMethodManager inputManager = (InputMethodManager)
				// editText
				// .getContext().getSystemService(
				// context.INPUT_METHOD_SERVICE);
				// inputManager.hideSoftInputFromInputMethod(
				// editText.getApplicationWindowToken(),
				// InputMethodManager.HIDE_NOT_ALWAYS);
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 5);
	}

	/**
	 * 切换软键盘显示和隐藏
	 */
	public static void toggleInput(final Context context,
			final EditText editText) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				InputMethodManager inputManager = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.toggleSoftInput(0,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 5);
	}
}
