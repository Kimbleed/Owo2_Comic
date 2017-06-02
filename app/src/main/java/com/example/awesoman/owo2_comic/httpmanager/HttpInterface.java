package com.example.awesoman.owo2_comic.httpmanager;

public class HttpInterface
{
    //普通请求网络时使用
    public interface GeneralCallback
    {
        void onSuccess(Object... data);
        void onError(int code, String error);
        void onWrong(int code, String error);
    }

	//需要刷新，翻页数据时使用
    public interface UpdateCallback
    {
        void onSuccess(Object... data);
        void onUpdate(Object... data);
        void onError(int code, String error);
        void onWrong(String error);
    }
}
