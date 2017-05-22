package com.example.awesoman.owo2_comic.ui.ComicLocal;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.awesoman.owo2_comic.model.ComicTypeBean;
import com.example.awesoman.owo2_comic.utils.MyLogger;
import com.example.awesoman.owo2_comic.view.CircleBorderView;
import com.example.awesoman.owo2_comic.utils.FileManager;
import com.example.awesoman.owo2_comic.R;
import com.example.awesoman.owo2_comic.model.ComicBean;
import com.example.awesoman.owo2_comic.ui.MainActivity;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.AddIntoAdapter;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ComicAdapter;
import com.example.awesoman.owo2_comic.ui.ComicLocal.adapter.ComicTypeAdapter;
import com.example.awesoman.owo2_comic.storage.ComicEntry;
import com.example.awesoman.owo2_comic.utils.LogUtil;
import com.example.awesoman.owo2_comic.utils.SkipUtil;
import com.google.gson.Gson;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Awesome on 2017/2/23.
 * 漫画本地书架Fragment
 */

public class ComicLocalFragment extends Fragment
        implements View.OnClickListener ,ComicAdapter.IComicHome,ComicTypeAdapter.IComicType{

    /**  - - - - - - - - -    控件部分 结束  - - - - - - - - - */
    @Bind(R.id.comicHomeRV)
    public RecyclerView comicHomeRV;
    @Bind(R.id.comicTypeRV)
    public RecyclerView comicTypeRV;
    @Bind(R.id.comicTypeRvLinear)
    public LinearLayout comicTypeRvLinear;
    @Bind(R.id.typeTxt)
    public TextView typeTxt;
    @Bind(R.id.cbv)
    public CircleBorderView cbv;
    @Bind(R.id.moreImg)
    public ImageView moreImg;
    @Bind(R.id.comicTypeLinear)
    public LinearLayout coimicTypeLinear;
    @Bind(R.id.editCheckImg)
    public ImageView editCheckImg;
    @Bind(R.id.addTypeBtn)
    LinearLayout addTypeBtn;

    EditText addComicTypeEditTxt;
    Dialog addTypeDialog = null;
    Dialog addInToDialog = null;
    /**  - - - - - - - - -    控件部分 结束  - - - - - - - - - */


    /**  - - - - - - - - -    变量部分 开始  - - - - - - - - -   */
    //漫画名List
    private List<String> comicNameList = null;
    //漫画种类List
    private List<ComicTypeBean> comicTypeList = null;
    //漫画实体类List  当前种类下的漫画List
    private List<ComicBean> comicHomeList = null;
    //需要删除的漫画list
    private List<String> listDelete;
    //文件管理类
    private FileManager fileManager = null;
    //适配器
    private ComicAdapter comicHomeAdapter = null;
    private ComicTypeAdapter comicTypeAdapter = null;
    //漫画类型 1.全部
    private int comicType = 0;
    //comicTypeRV 是否展开
    private boolean comicTypeIsShowing = false;
    private float xMoreImg = -1.0f;
    /**  - - - - - - - - -    变量部分 结束  - - - - - - - - - */



    /**  - - - - - - - - -    定量部分 结束  - - - - - - - - - */

    public static String TAG = "ComicLocalFragment";

    @Override
    public void onResume() {
        super.onResume();
        comicHomeList = fileManager.getComicMenuFromDB(comicTypeList.get(0).getComicTypeNo());
        comicHomeAdapter.notifyDataSetChanged();
    }

    /**  - - - - - - - - -    定量部分 结束  - - - - - - - - - */





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comic_home,null);
        ButterKnife.bind(this,view);

        //设置监听器
        coimicTypeLinear.setOnClickListener(this);
        editCheckImg.setOnClickListener(this);
        addTypeBtn.setOnClickListener(this);

        comicHomeAdapter = new ComicAdapter(getActivity(),this);
        comicTypeAdapter = new ComicTypeAdapter(getActivity(),this);

        //文件管理器 获取实例
        fileManager = FileManager.getInstance();

        scanFileAndRefreshDB();

        //获取漫画种类List
        comicTypeList = fileManager.getComicTypeFromDB();
        MyLogger.ddLog(TAG).i(new Gson().toJson(comicTypeList));
//        comicTypeList.add(0,"全部");

        //当前种类下的  漫画list
        if(comicTypeList !=null&& comicTypeList.size()!=0) {
            MyLogger.ddLog(TAG).i(comicTypeList.get(0).getComicTypeNo());
            comicHomeList = fileManager.getComicMenuFromDB(comicTypeList.get(0).getComicTypeNo());
        }

        //设置适配器
        comicHomeAdapter.setData(comicHomeList);
        comicTypeAdapter.setmData(comicTypeList);

        //设置recyclerView
        RecyclerView.LayoutManager comicTypeLM = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        comicTypeRV.setLayoutManager(comicTypeLM);
        comicTypeRV.setAdapter(comicTypeAdapter);


        RecyclerView.LayoutManager comicHomeLM = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        comicHomeRV.setLayoutManager(comicHomeLM);
        comicHomeRV.setAdapter(comicHomeAdapter);

        return view;
    }


    /**
     * 扫描文件夹ComicAll更新数据库
     */
    public void scanFileAndRefreshDB(){
        //更新数据库操作
        //先从数据库获取漫画目录  漫画名List,
        //若数据库中无数据,则从文件目录先遍历获取
        //并更新数据库
        if(fileManager.scanFile()) {
            comicNameList = fileManager.getComicNameListFromDB();
            if (comicNameList == null || comicNameList.size() <= 0) {
                comicNameList = fileManager.getNameListFromFile(ComicEntry.getComicPath());
                for (int i = 0; i < comicNameList.size(); i++) {
                    LogUtil.i("comicList_" + i, comicNameList.get(i));
                }
                fileManager.updateComicDB(comicNameList);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.comicTypeLinear:
                showComicTypeRV();
                break;
            case R.id.editCheckImg:
                if(comicHomeAdapter.isCheckBoxIsVisible()){
                    listDelete = comicHomeAdapter.choseForDelete();
//                        假删
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                for(String str:listDelete) {
//                                    LogUtil.i("deleteFile:", str);
//                                    FileUtils.delete(new File(str));
//                                }
//                                Toast.makeText(ComicLocalFragment.this.getContext(), "删除完成", Toast.LENGTH_SHORT).show();
//                            }
//                        }).start();
                        scanFileAndRefreshDB();
                        //当前种类下的  漫画list
                        if(comicTypeList !=null&& comicTypeList.size()!=0) {
                            comicHomeList = fileManager.getComicMenuFromDB(comicTypeList.get(0).getComicTypeNo());
                        }
                        //设置适配器
                        comicHomeAdapter.setData(comicHomeList);
                        comicHomeAdapter.notifyDataSetChanged();

                    comicHomeAdapter.setAllCheck(false);
                    editCheckImg.setBackground(getResources().getDrawable((R.drawable.edit_check_bg)));
                }else{
                    editCheckImg.setBackground(getResources().getDrawable(R.drawable.delete));
                }
                comicHomeAdapter.setCheckBoxIsVisible(!comicHomeAdapter.isCheckBoxIsVisible());
                comicTypeAdapter.setCheckBoxIsVisible(!comicTypeAdapter.isCheckBoxIsVisible());
                break;
            case R.id.addTypeBtn:
                showDialog();
                break;
        }
    }

    /**
     * 动画显示comicType
     */
    public void showComicTypeRV(){
        float width = comicTypeRvLinear.getWidth();
        //comicType弹出动画
        //1.moreImg先往右移，透明  淡出
        //2.CircleBorderView 展开
        //3.comicTypeRV展开

        // 1动画
        ValueAnimator alphaMoreImgAnimation = null;
        ValueAnimator translateMoreImgAnimation = null;

        // 3动画
        ValueAnimator translateAnimation = null;
        ValueAnimator alphaRVAnimation = null;

        AnimatorSet set = null;

        if(xMoreImg == -1){
            xMoreImg = moreImg.getX();
        }

        if(!comicTypeIsShowing) {
            //moreImg 伸缩动画
            translateMoreImgAnimation = new ObjectAnimator().ofFloat(moreImg,"x", xMoreImg, xMoreImg +10);
            translateMoreImgAnimation.setDuration(300);
            translateMoreImgAnimation.setInterpolator(new AccelerateInterpolator());
            translateMoreImgAnimation.setEvaluator(new FloatEvaluator());

            //moreImg 透明动画
            alphaMoreImgAnimation = new ObjectAnimator().ofFloat(moreImg,"alpha",1.0f,0.0f);
            alphaMoreImgAnimation.setDuration(300);
            alphaMoreImgAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaMoreImgAnimation.setEvaluator(new FloatEvaluator());

            //平移动画
            translateAnimation = new ObjectAnimator().ofFloat(comicTypeRvLinear,"x",0-width,0+typeTxt.getWidth());
            translateAnimation.setDuration(500);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            translateAnimation.setEvaluator(new FloatEvaluator());

            //透明度动画
            alphaRVAnimation = new ObjectAnimator().ofFloat(comicTypeRvLinear,"alpha",0.0f,1.0f);
            alphaRVAnimation.setDuration(500);
            alphaRVAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaRVAnimation.setEvaluator(new FloatEvaluator());

            alphaMoreImgAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    comicTypeRvLinear.setVisibility(View.VISIBLE);
                    // 2动画
                    cbv.clickAnimation(true);
                    cbv.setExpand(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });


            set = new AnimatorSet();
            set.play(translateMoreImgAnimation).with(alphaMoreImgAnimation);
            set.play(translateMoreImgAnimation).before(translateAnimation);
            set.play(translateAnimation).with(alphaRVAnimation);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    comicTypeRV.scrollToPosition(0);

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        else{
            //moreImg 伸缩动画
            translateMoreImgAnimation = new ObjectAnimator().ofFloat(moreImg,"x",xMoreImg+10, xMoreImg);
            translateMoreImgAnimation.setDuration(300);
            translateMoreImgAnimation.setInterpolator(new AccelerateInterpolator());
            translateMoreImgAnimation.setEvaluator(new FloatEvaluator());

            //moreImg 透明动画
            alphaMoreImgAnimation = new ObjectAnimator().ofFloat(moreImg,"alpha",0.0f,1.0f);
            alphaMoreImgAnimation.setDuration(300);
            alphaMoreImgAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaMoreImgAnimation.setEvaluator(new FloatEvaluator());

            //平移动画
            translateAnimation = new ObjectAnimator().ofFloat(comicTypeRvLinear,"x",0+typeTxt.getWidth(),0-width);
            translateAnimation.setDuration(500);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            translateAnimation.setEvaluator(new FloatEvaluator());

            //透明度动画
            alphaRVAnimation = new ObjectAnimator().ofFloat(comicTypeRvLinear,"alpha",1.0f,0.0f);
            alphaRVAnimation.setDuration(500);
            alphaRVAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            alphaRVAnimation.setEvaluator(new FloatEvaluator());


            set = new AnimatorSet();
            set.play(translateAnimation).with(alphaRVAnimation);
            set.play(translateMoreImgAnimation).after(translateAnimation);
            set.play(translateMoreImgAnimation).with(alphaMoreImgAnimation);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    cbv.clickAnimation(false);
                    cbv.setExpand(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    comicTypeRvLinear.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        set.start();
        comicTypeIsShowing = !comicTypeIsShowing;

    }

    @Override
    public void onComicHomeItemClick(int position) {
        Intent intent = new Intent(getContext(),ComicChapterActivity.class);
        intent.putExtra("comicBean",comicHomeList.get(position));
        SkipUtil.skip(getContext(),intent,false);
        ((MainActivity)getContext()). jumpAnimation(1);
    }

    @Override
    public void onComicTypeDelete(int position) {
        LogUtil.i("onComicTypeDelete",position+"");
        comicTypeAdapter.notifyItemRemoved(position);
        fileManager.deleteComicTypeByName(comicTypeList.get(position).getComicTypeName());
        comicTypeList.remove(position);
    }

    @Override
    public void onComicTypeItemClick(int position) {
        LogUtil.i("onComicTypeItemClick",position+"");
        typeTxt.setText(comicTypeList.get(position).getComicTypeName());
        comicType = position;
        comicHomeList =fileManager.getComicMenuFromDB(position+1);

    }


    /**
     * 弹出添加漫画种类Dialog
     */
    public void initAddTypeNameDialog(){
/*        addTypeDialog = new AlertDialog.Builder(getContext()).create();
        Window window = addTypeDialog.getWindow();
        window.setContentView(R.layout.dialog_add_comic_type);
        Button btnOk = (Button)window.findViewById(R.id.btn_ok);
        Button btnCancel = (Button)window.findViewById(R.id.btn_cancel);
        addComicTypeEditTxt = (EditText)window.findViewById(R.id.addComicTypeEditTxt);
        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id){
                    case R.id.btn_ok:
                        fileManager.addComicType(addComicTypeEditTxt.getText().toString());
                        comicTypeList = fileManager.getComicTypeFromDB();
                        comicTypeAdapter.setData(comicTypeList);
                        comicTypeAdapter.notifyDataSetChanged();
                        initAddComicInToTypeDialog(addComicTypeEditTxt.getText().toString());
//                        comicTypeRV.scro
                    case R.id.btn_cancel:
                        addTypeDialog.dismiss();
                        break;
                }
            }
        };
        btnOk.setOnClickListener(dialogClickListener);
        btnCancel.setOnClickListener(dialogClickListener);
        StringUtils.showInputMethod(getContext(),addComicTypeEditTxt);
        addTypeDialog.show();*/



        addTypeDialog = new Dialog(getContext(), R.style.customDialog);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_add_comic_type, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        Button btnOk = (Button)layout.findViewById(R.id.btn_ok);
        Button btnCancel = (Button)layout.findViewById(R.id.btn_cancel);
        addComicTypeEditTxt = (EditText)layout.findViewById(R.id.addComicTypeEditTxt);

        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id){
                    case R.id.btn_ok:
                        fileManager.addComicType(addComicTypeEditTxt.getText().toString());
                        comicTypeList = fileManager.getComicTypeFromDB();
                        comicTypeAdapter.setmData(comicTypeList);
                        comicTypeAdapter.notifyDataSetChanged();
                        initAddComicInToTypeDialog(addComicTypeEditTxt.getText().toString());
//                        comicTypeRV.scro
                    case R.id.btn_cancel:
                        addTypeDialog.dismiss();
                        break;
                }
            }
        };
        btnOk.setOnClickListener(dialogClickListener);
        btnCancel.setOnClickListener(dialogClickListener);

        Window w = addTypeDialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = 0;
        lp.gravity = Gravity.CENTER;
        addTypeDialog.onWindowAttributesChanged(lp);
        addTypeDialog.setContentView(layout);
        addTypeDialog.setCanceledOnTouchOutside(true);
        addTypeDialog.show();
    }

    public void showDialog(){
        if(addTypeDialog !=null) {
            if (addTypeDialog.isShowing()) {
                addTypeDialog.dismiss();
            }
            else{
                initAddTypeNameDialog();

            }
        }
        else{
            initAddTypeNameDialog();
        }
    }

    /**
     * 弹出添加漫画到种类下 的 Dialog
     */
    public void initAddComicInToTypeDialog(final String type){
        addInToDialog = new AlertDialog.Builder(getContext()).create();
        addInToDialog.show();
        Window window = addInToDialog.getWindow();
        window.setContentView(R.layout.dialog_add_comic_into);
        Button btnOk = (Button)window.findViewById(R.id.btn_ok);
        Button btnCancel = (Button)window.findViewById(R.id.btn_cancel);
        final ListView listView = (ListView)window.findViewById(R.id.comicChoseList);
        final AddIntoAdapter adapter = new AddIntoAdapter(getContext());
        //所有漫画
        List<ComicBean> comicAll = fileManager.getComicMenuFromDB(1);
        adapter.setData(comicAll);
        listView.setAdapter(adapter);
        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id){
                    case R.id.btn_ok:
                        List<ComicBean> chose = adapter.getChoseComicList();
                        for(int i = 0;i< chose.size();i++){
                            LogUtil.i("chose_"+i,chose.get(i).getComicName());
                        }
                        fileManager.updateComicType(chose,type);
                    case R.id.btn_cancel:
                        addInToDialog.dismiss();
                        break;
                }
            }
        };
        btnOk.setOnClickListener(dialogClickListener);
        btnCancel.setOnClickListener(dialogClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLogger.ddLog(TAG).i("ComicLocalFragment onDestroy");
    }
}
