package com.mozilla.hackathon.twigamsituni.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mozilla.hackathon.twigamsituni.R;

public class OverLayActivity extends AppCompatActivity {

    TextView textName;
    ImageView close;

    public static Intent newInstance(Context c, String name) {

        Intent intent = new Intent(c, OverLayActivity.class);
        intent.putExtra("Name", name);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //getWindow().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setContentView(R.layout.activity_over_lay);
        /**
         * getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
         getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
         */

        textName = (TextView)findViewById(R.id.activity_name);
        close = (ImageView)findViewById(R.id.close);

        textName.setText(getIntent().getStringExtra("Name"));
    }
}
