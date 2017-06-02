package com.example.awesoman.owo2_comic.httpmanager;

import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jzhao on 2016/11/15.
 */
public class HttpManager
{
    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public static void post(String url, RequestParams params, ResponseHandlerInterface responseHandler)
    {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());

        String sign = getSign(url, 1, params);
//        params.put(HttpParam.REQUEST_PARAM_SIGN, sign);
        mClient.post(url, buildRequestParams(params), responseHandler);
    }



    public static RequestParams getGeneralParam()
    {
        RequestParams params = new RequestParams();

//        String token = GlobalParam.mUserInfo.getAccessToken();
//        String phoneNum = GlobalParam.mUserInfo.getPhone();
//        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(phoneNum))
//        {
//            return null;
//        }

//        String sign = DingUtils.toMd5(phoneNum + ":" + token + ":/" + method);

//        params.put(HttpParam.REQUEST_PARAM_TOKEN, token);
//        params.put(HttpParam.SERVER_PARAM_TIMESTAMP, System.currentTimeMillis());
        //params.put(HttpParam.REQUEST_PARAM_SIGN, sign);
        params.put(HttpParam.KEY, "645f235cd82d718b6dea6a62bf3d7123");
        //params.put("8da5ee01647719e67c49d82d", "87f2be668896e243c4c13755b3b9ed9d");

        return params;
    }

    public static void get(String url, RequestParams params, ResponseHandlerInterface responseHandler)
    {
        SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());

        String sign = getSign(url, 0, params);
//        params.put(HttpParam.REQUEST_PARAM_SIGN, sign);
        mClient.get(url, buildRequestParams(params), responseHandler);
    }

    public static void put(String url, RequestParams params, ResponseHandlerInterface responseHandler)
    {
        String sign = getSign(url, 2, params);
//        params.put(HttpParam.REQUEST_PARAM_SIGN, sign);
        mClient.put(url, buildRequestParams(params), responseHandler);
    }

    private static RequestParams buildRequestParams(RequestParams params)
    {
        params = ( null == params ) ? new RequestParams() : params;
        return params;
    }

    private static String getSign(String url, int method, RequestParams params)
    {
        String sign = "";
        String signString = "";
        if ( method == 0 )
        {
            signString = "get";
        }
        else if ( method == 1 )
        {
            signString = "post";
        }
        else
        {
            signString = "put";
        }

//        if ( url.contains(HttpUrl.PASSPORT_SERVER_URL) )
//        {
//            int start = HttpUrl.PASSPORT_SERVER_URL.length();
//            String path = url.substring(start);
//            signString += path;
//        }
//        else if( url.contains(HttpUrl.SERVER_URL) )
//        {
//            int start = HttpUrl.SERVER_URL.length();
//            String path = url.substring(start);
//            signString += path;
//        }else if( url.contains(HttpUrl.PEEP_HOLE_SERVER_URL) ){
//            int start = HttpUrl.PEEP_HOLE_SERVER_URL.length();
//            String path = url.substring(start);
//            signString += path;
//        }

        String paramsStr = params.toString();
        String[] allParams = paramsStr.split("&");

        List<KeyValueInfo> paramList = new ArrayList<>();

        for ( int i = 0; i < allParams.length; i++ )
        {
            String[] singleParam = allParams[i].split("=");

            KeyValueInfo singlekv = new KeyValueInfo();
            singlekv.setKey(singleParam[0]);

            if ( singleParam.length > 1 )
            {
                singlekv.setValue(singleParam[1]);
            }

            paramList.add(singlekv);
        }

        Collections.sort(paramList, new ComparatorParam());

        for ( int i = 0; i < paramList.size(); i++ )
        {
            signString = signString + paramList.get(i).getKey();
            signString = signString + "=" + paramList.get(i).getValue();

            if ( i < (paramList.size() - 1) )
            {
                signString = signString + "&";
            }
        }

        signString += "87f2be668896e243c4c13755b3b9ed9d";

        //signString = ";/?:@&=+$,#aaa-_.!~*'()bbb[]{}eee <^%|>\"";
        signString = toURLEncoded(signString);

//        sign = DingUtils.toMd5(signString);

        return sign;
    }

    private static class KeyValueInfo
    {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class ComparatorParam implements Comparator<KeyValueInfo>
    {
        @Override
        public int compare(KeyValueInfo kv1, KeyValueInfo kv2)
        {
            return kv1.getKey().compareTo(kv2.getKey());
        }
    }

    public static String toURLEncoded(String paramString)
    {
        if (paramString == null || paramString.equals(""))
        {
            return "";
        }

        try
        {
            String str = new String(paramString.getBytes());
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
        }

        return "";
    }
}
