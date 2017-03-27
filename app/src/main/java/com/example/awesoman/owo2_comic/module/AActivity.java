package com.example.awesoman.owo2_comic.module;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.awesoman.owo2_comic.R;

public class AActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
    }

    public void jump(View view){
        startActivity(new Intent().setClass(this,BActivity.class));
    }
}
