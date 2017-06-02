package com.example.awesoman.owo2_comic.ui.ComicOnline;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.httpmanager.HttpInterface;
import com.example.awesoman.owo2_comic.httpmanager.HttpManager;
import com.example.awesoman.owo2_comic.httpmanager.HttpMethod;
import com.example.awesoman.owo2_comic.model.ComicChapterResult;
import com.example.awesoman.owo2_comic.model.HttpComicChapterInfo;
import com.example.awesoman.owo2_comic.model.HttpComicInfo;
import com.example.awesoman.owo2_comic.storage.ComicTypeInfoDao;
import com.example.awesoman.owo2_comic.ui.BaseActivity;
import com.example.awesoman.owo2_comic.ui.ComicLocal.ComicChapterActivity;
import com.example.awesoman.owo2_comic.utils.DensityUtils;
import com.example.awesoman.owo2_comic.utils.FastBlur;
import com.example.awesoman.owo2_comic.utils.MyLogger;
import com.example.awesoman.owo2_comic.view.MyPtrHead;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Awesome on 2017/6/1.
 */

public class ComicOnlineChapterActivity extends BaseActivity implements ComicChapterAdapter.IComicChapterListener {

    public static final String TAG = "ComicOnlineChapterActivity";

    @Bind(R.id.iv_bg)
    ImageView iv_bg;

    @Bind(R.id.chapter_container)
    RecyclerView chapterContainer;

    @Bind(R.id.ptr)
    PtrClassicFrameLayout ptrLayout;


    private HttpComicInfo mHttpComicInfo;

    private List<HttpComicChapterInfo> mData = new ArrayList<>();
    ComicChapterAdapter mComicChapterAdapter;
    private int widthImgBg = 120, heightImgBg = 150;

    private static final int REFRESH_UI = 100;

    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what)
            {
                case REFRESH_UI:
                    showChapter();
                    break;
            }
        }
    };

    @Override
    public int getContentViewID() {
        return R.layout.activity_chapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChapter();
    }

    @Override
    public void initView() {
        mHttpComicInfo = (HttpComicInfo) getIntent().getSerializableExtra("httpComicInfo");
//        showChapter();
        initPtr();
//        setBg(widthImgBg, heightImgBg);
    }

    public void setBg(int dpWidth, int dpHeight) {
        //获取图片
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(mHttpComicInfo.getCoverImg());
//        bitmap = comicDBManager.makeSurface(bitmap,CTX.getResources().getDimensionPixelSize(R.dimen.surface_comic_list_width),CTX.getResources().getDimensionPixelSize(R.dimen.surface_comic_list_height));
        //模糊处理
        iv_bg.setImageBitmap(FastBlur.doBlur(bitmap, 50, false));
    }

    public void showChapter() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                mComicChapterAdapter = new ComicChapterAdapter(ComicOnlineChapterActivity.this, ComicOnlineChapterActivity.this);
                mComicChapterAdapter.setmData(mData);
                mComicChapterAdapter.setmTitle(mHttpComicInfo.getName());
                mComicChapterAdapter.setmType(mHttpComicInfo.getType());
                mComicChapterAdapter.setmSurPath(mHttpComicInfo.getCoverImg());
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                GridLayoutManager layoutManager = new GridLayoutManager(ComicOnlineChapterActivity.this, 3);
                GridLayoutManager.SpanSizeLookup bbb = new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return mComicChapterAdapter.getItemViewType(position);
                    }
                };
                layoutManager.setSpanSizeLookup(bbb);
                chapterContainer.setLayoutManager(layoutManager);
                chapterContainer.setAdapter(mComicChapterAdapter);
                chapterContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        MyLogger.ddLog(TAG).i("x:" + dx + "|||y:" + dy);
                    }
                });
            }
        }.execute();
    }

    public void initPtr() {
        ptrLayout.setLastUpdateTimeRelateObject(this);
//        ptrLayout.setBackgroundResource((R.color.white));
        ptrLayout.setDurationToCloseHeader(1000);
        MyPtrHead head = new MyPtrHead(this);
        head.setListener(new MyPtrHead.IPtrHeadListener() {
            @Override
            public void onDownMoveEvent(float percent) {
//                setBg(widthImgBg + ((int)(0.8*percent*100)),heightImgBg+((int)percent*100));
            }
        });
        ptrLayout.setHeaderView(head);
        ptrLayout.addPtrUIHandler(head);
        ImageView foot = new ImageView(this);
        ptrLayout.setFooterView(foot);
        ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            }
        });
    }

    public void getChapter() {
        RequestParams params = HttpManager.getGeneralParam();
        params.put("comicName", mHttpComicInfo.getName());
        params.put("skip", "");
        HttpMethod.getHttpComicChapterInfo(this, params, new HttpInterface.GeneralCallback() {
            @Override
            public void onSuccess(Object... data) {
                mData.clear();
                mData.addAll(((ComicChapterResult) data[0]).getBookList());
                mUIHandler.sendEmptyMessage(REFRESH_UI);
            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onWrong(int code, String error) {

            }
        });
    }

    @Override
    public void onChapterClick(int position) {
        Intent intent = new Intent(this,ComicOnlineReadActivity.class);
        intent.putExtra("http_comic_chapter_info",mData.get(position));
        intent.putExtra("http_comic_info",mHttpComicInfo);
        startActivity(intent);
    }

    @Override
    public void onSurClick() {

    }
}
