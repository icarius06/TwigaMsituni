package com.mozilla.hackathon.twigamsituni.datausage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mozilla.hackathon.twigamsituni.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TrafficStatsSummaryFragment extends Fragment {

    @Bind(R.id.traffic_stats_recycler)
    public RecyclerView recyclerView;
    @Bind(R.id.header)
    public LinearLayout headerBottom;
    private float initialHeaderSize;

    public TrafficStatsSummaryFragment() {
        // Required empty public constructor
    }

    LinearLayoutManager layoutManager;
    DataSummaryAdapter dataSummaryAdapter;


    public static TrafficStatsSummaryFragment newInstance() {
        TrafficStatsSummaryFragment fragment = new TrafficStatsSummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_traffic_stats_summary, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        dataSummaryAdapter = new DataSummaryAdapter(getActivity());
        recyclerView.setAdapter(dataSummaryAdapter);
        initialHeaderSize = getResources().getDimension(R.dimen.header_height);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (layoutManager.findFirstVisibleItemPosition() > 0) {
                    reactToScroll((int) -initialHeaderSize);
                } else {
                    reactToScroll(layoutManager.getChildAt(0).getTop());
                }

            }
        });
        return view;
    }

    private void reactToScroll(int position) {

        if (-position >= initialHeaderSize) {
            headerBottom.setVisibility(View.GONE);
        } else {
            headerBottom.setVisibility(View.VISIBLE);

            float delta = (initialHeaderSize - (-position));
            float deltaPercentage = delta / initialHeaderSize;

            ViewGroup.LayoutParams params = headerBottom.getLayoutParams();
            params.height = (int) delta;
            headerBottom.setLayoutParams(params);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void createDummyData(){
        List<DataRecord> data=new ArrayList<DataRecord>();
        DataRecord dataRecord=new DataRecord();
        dataRecord.appName="Facebook";
        dataRecord.costPerMb="1000";
        dataRecord.usage="100";
        data.add(dataRecord);
        dataRecord=new DataRecord();
        dataRecord.appName="Whatsapp";
        dataRecord.costPerMb="1000";
        dataRecord.usage="100";
        data.add(dataRecord);
        dataRecord=new DataRecord();
        dataRecord.appName="Twitter";
        dataRecord.costPerMb="1000";
        dataRecord.usage="100";
        data.add(dataRecord);
        dataSummaryAdapter.setDataRecordList(data);
        dataSummaryAdapter.notifyDataSetChanged();
    }
}
