package com.example.awesoman.owo2_comic.ui.ComicOnline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.httpmanager.HttpInterface;
import com.example.awesoman.owo2_comic.httpmanager.HttpManager;
import com.example.awesoman.owo2_comic.httpmanager.HttpMethod;
import com.example.awesoman.owo2_comic.model.ComicBookResult;
import com.example.awesoman.owo2_comic.model.ComicInfo;
import com.example.awesoman.owo2_comic.model.HttpComicInfo;
import com.example.awesoman.owo2_comic.utils.MyLogger;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Awesome on 2017/3/3.
 */

public class ComicOnlineFragment extends Fragment implements ComicAdapter.IComicHome {

    public static String TAG = "ComicOnlineFragment";

    private RecyclerView recycler_book;
    private PtrClassicFrameLayout ptrLayout;
    private List<HttpComicInfo> mData =new ArrayList<>();
    private ComicAdapter mComicAdapter;
    private int page = 1;

    private static final int REFRESH_UI = 100;

    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_UI:
                    mComicAdapter.notifyDataSetChanged();
                    ptrLayout.refreshComplete();
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comic_online,null);
        recycler_book = (RecyclerView)view.findViewById(R.id.recycler_book);
        ptrLayout = (PtrClassicFrameLayout)view.findViewById(R.id.ptr);
        initRecycler();
        initPtr();
        return view;
    }

    public void initRecycler(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);

        recycler_book.setLayoutManager(layoutManager);

        mComicAdapter = new ComicAdapter(getContext(),this);
        mComicAdapter.setData(mData);

        recycler_book.setAdapter(mComicAdapter);
    }

    public void initPtr() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setDurationToCloseHeader(1000);
        ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                getHttpComicInfo(false);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getHttpComicInfo(true);
            }
        });

    }

    @Override
    public void onComicHomeItemClick(int position) {
        Intent intent = new Intent(getContext(),ComicOnlineChapterActivity.class);
        intent.putExtra("httpComicInfo",mData.get(position));
        startActivity(intent);
    }

    @Override
    public void onContinueClick(ComicInfo info, String chapter, int page) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getHttpComicInfo(true);
    }

    public void getHttpComicInfo(final boolean isRefresh){
        RequestParams params = HttpManager.getGeneralParam();
        params.put("name","");
        params.put("type","");
        if(mData.size() == 0 || isRefresh)
            params.put("skip","");
        else
            params.put("skip",mData.size());
        params.put("finish","");

        HttpMethod.getHttpComicBookInfo(getContext(), params, new HttpInterface.GeneralCallback() {
            @Override
            public void onSuccess(Object... data) {
                if(isRefresh){
                    mData.clear();
                    mData .addAll(((ComicBookResult)data[0]).getBookList());
                    page = 0;
                }
                else{
                    mData .addAll(((ComicBookResult)data[0]).getBookList());
                    page ++;
                }

                mUIHandler.sendEmptyMessage(REFRESH_UI);
            }

            @Override
            public void onError(int code, String error) {
                MyLogger.ddLog(TAG).i(error);
            }

            @Override
            public void onWrong(int code, String error) {
                MyLogger.ddLog(TAG).i(error);
            }
        });
    }
}
