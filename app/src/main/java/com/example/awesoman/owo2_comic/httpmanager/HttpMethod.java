package com.example.awesoman.owo2_comic.httpmanager;

import android.content.Context;

import com.example.awesoman.owo2_comic.model.ComicBookResult;
import com.example.awesoman.owo2_comic.model.ComicChapterResult;
import com.example.awesoman.owo2_comic.model.ComicContentResult;
import com.example.awesoman.owo2_comic.utils.MyLogger;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by Awesome on 2017/6/1.
 */

public class HttpMethod {

    public static final String TAG = "HttpMethod";

    public static void getHttpComicBookInfo(Context context, RequestParams params, final HttpInterface.GeneralCallback actionListener){
        String url = HttpUrl.SERVER_URL + HttpUrl.GET_BOOK;
        HttpManager.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                try {
                    String responseString = new String(bytes, "UTF-8");
                    MyLogger.ddLog(TAG).i("getHttpComicBookInfo responseString:" + responseString);

                    JSONObject responseObj = new JSONObject(responseString);

                    int errorCode = responseObj.getInt("error_code");
                    String errorMsg = responseObj.getString("reason");

                    ComicBookResult result = new Gson().fromJson(responseObj.getString("result"),ComicBookResult.class);

                    if ( errorCode == 200 )
                    {
                        actionListener.onSuccess(result);
                    }
                    else
                    {
                        actionListener.onWrong(errorCode, errorMsg);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                actionListener.onError(i,"网络错误，请检查网络");
            }
        });

    }

    public static void getHttpComicChapterInfo(Context context, RequestParams params, final HttpInterface.GeneralCallback actionListener){
        String url = HttpUrl.SERVER_URL + HttpUrl.GET_CHAPTER;
        HttpManager.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                try {
                    String responseString = new String(bytes, "UTF-8");
                    MyLogger.ddLog(TAG).i("getHttpComicBookInfo responseString:" + responseString);

                    JSONObject responseObj = new JSONObject(responseString);

                    int errorCode = responseObj.getInt("error_code");
                    String errorMsg = responseObj.getString("reason");

                    ComicChapterResult result = new Gson().fromJson(responseObj.getString("result"),ComicChapterResult.class);

                    if ( errorCode == 200 )
                    {
                        actionListener.onSuccess(result);
                    }
                    else
                    {
                        actionListener.onWrong(errorCode, errorMsg);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                actionListener.onError(i,"网络错误，请检查网络");
            }
        });

    }

    public static void getHttpComicChapterContentInfo(Context context, RequestParams params, final HttpInterface.GeneralCallback actionListener){
        String url = HttpUrl.SERVER_URL + HttpUrl.GET_CONTENT;
        HttpManager.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                try {
                    String responseString = new String(bytes, "UTF-8");
                    MyLogger.ddLog(TAG).i("getHttpComicChapterContentInfo responseString:" + responseString);

                    JSONObject responseObj = new JSONObject(responseString);

                    int errorCode = responseObj.getInt("error_code");
                    String errorMsg = responseObj.getString("reason");

                    ComicContentResult result = new Gson().fromJson(responseObj.getString("result"),ComicContentResult.class);

                    if ( errorCode == 200 )
                    {
                        actionListener.onSuccess(result);
                    }
                    else
                    {
                        actionListener.onWrong(errorCode, errorMsg);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                actionListener.onError(i,"网络错误，请检查网络");
            }
        });

    }
}
