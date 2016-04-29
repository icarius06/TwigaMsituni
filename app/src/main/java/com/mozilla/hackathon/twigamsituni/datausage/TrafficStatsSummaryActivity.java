package com.mozilla.hackathon.twigamsituni.datausage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mozilla.hackathon.twigamsituni.R;

public class TrafficStatsSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_stats_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_view, TrafficStatsSummaryFragment.newInstance()).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                commit();
    }

}
