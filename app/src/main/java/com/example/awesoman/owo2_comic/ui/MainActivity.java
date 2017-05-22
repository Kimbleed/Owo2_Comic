package com.example.awesoman.owo2_comic.ui;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.storage.ComicEntry;
import com.example.awesoman.owo2_comic.storage.Constants;
import com.example.awesoman.owo2_comic.ui.ComicOnline.ComicOnlineFragment;
import com.example.awesoman.owo2_comic.ui.Music.MusicFragment;
import com.example.awesoman.owo2_comic.ui.Setting.SettingFragment;
import com.example.awesoman.owo2_comic.ui.ComicLocal.ComicLocalFragment;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.utils.FileUtils;
import com.example.awesoman.owo2_comic.utils.LogUtil;
import com.example.awesoman.owo2_comic.utils.SPUtil;

import java.io.File;

import butterknife.Bind;

/**
 * Created by Awesome on 2017/2/20.
 * 漫画首页Activity
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.myViewPager)
    public ViewPager myViewPager;
    @Bind({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    LinearLayout btnViews[];
    @Bind({R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4})
    ImageView imgViews[];
    @Bind({R.id.pageTxt1, R.id.pageTxt2, R.id.pageTxt3, R.id.pageTxt4})
    TextView TxtViews[];

    @Override
    public int getContentViewID() {
        return R.layout.activity_comic_home;
    }

    @Override
    public void initView() {

        boolean isFirstAsk = (boolean) SPUtil.getInstance(this).get(Constants.FIRST_REQUEST_FILE_PERMISSION, true);
        if(isFirstAsk) {
            FileManager.getInstance().addComicType("全部");
            SPUtil.getInstance(this).put(Constants.FIRST_REQUEST_FILE_PERMISSION, false);
            //请求写文件权限
            ActivityCompat.requestPermissions(this, new String[]{android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        myViewPager.setAdapter(fragmentPagerAdapter);
        for (LinearLayout ll : btnViews) {
            ll.setOnClickListener(this);
        }
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (ImageView img : imgViews) {
                    int id = img.getId();
                    int resId = 0;
                    switch (id) {
                        case R.id.icon1:
                            if (position == 0) {
                                resId = R.drawable.bookrack_icon_select;
                            } else
                                resId = R.drawable.bookrack_icon_default;
                            break;
                        case R.id.icon2:
                            if (position == 1)
                                resId = R.drawable.comic_online_icon_select;
                            else
                                resId = R.drawable.comic_online_icon;
                            break;
                        case R.id.icon3:
                            if (position == 2)
                                resId = R.drawable.music_icon_select;
                            else
                                resId = R.drawable.music_icon;
                            break;
                        case R.id.icon4:
                            if (position == 3)
                                resId = R.drawable.more_select_setting_select;
                            else
                                resId = R.drawable.more_select_setting;
                            break;
                    }
                    img.setBackground(getResources().getDrawable(resId));
                    for (int i = 0; i < 4; i++) {
                        if (i == position)
                            TxtViews[i].setTextColor(getResources().getColor(R.color.red));
                        else
                            TxtViews[i].setTextColor(getResources().getColor(R.color.grayBlack));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

        Fragment[] fragments = new Fragment[4];

        @Override
        public Fragment getItem(int position) {
            LogUtil.i("cenjunhui", position + "");
            switch (position) {
                case 0:
                    if (fragments[0] == null)
                        fragments[0] = new ComicLocalFragment();
                    return fragments[0];
                case 1:
                    if (fragments[1] == null)
                        fragments[1] = new ComicOnlineFragment();
                    return new ComicOnlineFragment();
                case 2:
                    if (fragments[2] == null)
                        fragments[2] = new MusicFragment();
                    return new MusicFragment();
                case 3:
                    if (fragments[3] == null)
                        fragments[3] = new SettingFragment();
                    return new SettingFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn1:
                myViewPager.setCurrentItem(0);
                break;
            case R.id.btn2:
                myViewPager.setCurrentItem(1);
                break;
            case R.id.btn3:
                myViewPager.setCurrentItem(2);
                break;
            case R.id.btn4:
                myViewPager.setCurrentItem(3);
//                startActivity(new Intent(this,ComicReadActivity.class));
//                jumpAnimation();
                break;
        }
    }


    //控制页面跳转动画
    public void jumpAnimation(int direction) {
        if (direction == 1)
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        else if (direction == 2)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File fileProject = new File(ComicEntry.getProjectPath());
                        File fileMusic = new File( ComicEntry.getMusicPath());
                        File fileComic = new File( ComicEntry.getComicPath());
                        if (!fileProject.exists()) {
                            Log.i("mkdirs",fileProject.getAbsolutePath()+"|"+fileProject.mkdirs());
                            Log.i("mkdirs",fileProject.getAbsolutePath()+"|"+fileMusic.mkdirs());
                            Log.i("mkdirs",fileProject.getAbsolutePath()+"|"+fileComic.mkdirs());

                        }
                        FileUtils.copyFile(getResources().openRawResource(R.raw.someone_like_you),"someone_like_you.mp3", ComicEntry.getMusicPath());
                        FileUtils.copyFile(getResources().openRawResource(R.raw.yui_how_crazy_2),"yui_how_crazy_2.mp3", ComicEntry.getMusicPath()
                        );
                    }
                    break;
                }
        }
    }
}
