package com.example.awesoman.owo2_comic.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Awesome on 2017/2/26.
 */

public class StringUtils {
    public static boolean isEmpty(String str){
        if(str==null || str.length()==0)
            return true;
        return false;
    }

    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
