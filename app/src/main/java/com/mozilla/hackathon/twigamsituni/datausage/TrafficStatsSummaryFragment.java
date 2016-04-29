package com.mozilla.hackathon.twigamsituni.datausage;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mozilla.hackathon.twigamsituni.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TrafficStatsSummaryFragment extends Fragment implements OnChartValueSelectedListener {

    @Bind(R.id.traffic_stats_recycler)
    public RecyclerView recyclerView;
    @Bind(R.id.header)
    public LinearLayout headerBottom;
    private float initialHeaderSize;
    @Bind(R.id.stats_summary)
    PieChart mChart;
    Typeface tf;
    private long rx,tx;
    private float rate=0.30f;

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

    private void createCharts() {
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);


        mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(getString(R.string.data));

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(2, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        yVals1.add(new Entry(TrafficStats.getTotalRxBytes(), 0));
        yVals1.add(new Entry(TrafficStats.getTotalTxBytes(), 1));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, getString(R.string.data));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
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

    @Override
    public void onResume() {
        super.onResume();
        loadAppData();
        createCharts();
    }

    private void createDummyData() {
        List<DataRecord> data = new ArrayList<DataRecord>();
        DataRecord dataRecord = new DataRecord();
        dataRecord.appName = "Facebook";
        dataRecord.costPerMb = "1000";
        dataRecord.usage = "100";
        data.add(dataRecord);
        dataRecord = new DataRecord();
        dataRecord.appName = "Whatsapp";
        dataRecord.costPerMb = "1000";
        dataRecord.usage = "100";
        data.add(dataRecord);
        dataRecord = new DataRecord();
        dataRecord.appName = "Twitter";
        dataRecord.costPerMb = "1000";
        dataRecord.usage = "100";
        data.add(dataRecord);
        dataSummaryAdapter.setDataRecordList(data);
        dataSummaryAdapter.notifyDataSetChanged();
    }

    protected String[] mParties = new String[]{
            "Data In", "Data Out"
    };

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void loadAppData(){
        List<DataRecord>dataRecords=new ArrayList<DataRecord>();
        for (ApplicationInfo app :
                getActivity().getPackageManager().getInstalledApplications(0)) {
            DataRecord dataRecord=new DataRecord();
            dataRecord.usage=String.valueOf(TrafficStats.getUidTxBytes(app.uid));
            dataRecord.costPerMb=String.valueOf(TrafficStats.getUidTxBytes(app.uid)*rate);
            dataRecord.appName= (String) app.loadLabel(getActivity().getPackageManager());
            dataRecord.icon=app.loadIcon(getActivity().getPackageManager());
            dataRecord.rate=rate;
            dataRecords.add(dataRecord);
        }
        dataSummaryAdapter.setDataRecordList(dataRecords);
        dataSummaryAdapter.notifyDataSetChanged();
    }


}
