package com.mozilla.hackathon.twigamsituni.datausage;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mozilla.hackathon.twigamsituni.R;
import com.mozilla.hackathon.twigamsituni.Utils;
import com.mozilla.hackathon.twigamsituni.datausage.datausage.persist.DataOperation;
import com.mozilla.hackathon.twigamsituni.datausage.datausage.persist.DeviceData;
import com.mozilla.hackathon.twigamsituni.datausage.datausage.persist.TrafficRecordUpdateReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class TrafficStatsSummaryFragment extends Fragment implements OnChartValueSelectedListener {

    @Bind(R.id.traffic_stats_recycler)
    public RecyclerView recyclerView;
    @Bind(R.id.header)
    public LinearLayout headerBottom;
    private float initialHeaderSize;
    @Bind(R.id.stats_summary)
    LineChartView mChart;
    @Bind(R.id.time_range)
    RangeBar rangeBar;
    @Bind(R.id.data_size)
    EditText dataSize;
    @Bind(R.id.data_cost)
    EditText dataCost;
    @Bind(R.id.enter_rate)
    LinearLayout enterDataView;
    @Bind(R.id.computed_rate)
    TextView computedRate;
    Typeface tf;
    private long rx, tx;
    private float rate = 0.30f;
    private int range = 10;
    private List<DeviceData> dataRecords;
    private int cost;
    private int volume;

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
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

            }
        });
        rangeBar.setTickEnd(range);
        createCharts();
        dataSize.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (textView.getText() != null) {
                    String value = textView.getText().toString();
                    volume = Integer.valueOf(value);
                }
                return false;
            }
        });
        dataCost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (textView.getText() != null) {
                    String value = textView.getText().toString();
                    cost = Integer.valueOf(value);
                }
                return false;
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
        rangeBar.setSeekPinByIndex(range);
    }

    private void createCharts() {
        mChart.setInteractive(false);
        mChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        mChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(showDeviceDataSummary());

        line.setColor(ChartUtils.COLORS[0]);
        line.setHasLabels(true);
        line.setHasLines(true);
        //line.setHasPoints(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("TIME");
        axisY.setName("VOLUME (DATA)");

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        mChart.setLineChartData(data);
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

    private void loadAppData() {
        List<DataRecord> dataRecords = new ArrayList<DataRecord>();
        for (ApplicationInfo app :
                getActivity().getPackageManager().getInstalledApplications(0)) {
            DataRecord dataRecord = new DataRecord();
            DataRecord usage = DataOperation.getAppTrafficById(app.uid, 0);
            String dataUsage = usage.usage;
            //if (Long.valueOf(dataUsage) > 0)
            {
                dataRecord.usage = Utils.humanReadableByteCount(Long.valueOf(dataUsage), true);
                dataRecord.costPerMb = String.valueOf(Utils.bytesToMeg(Long.valueOf(dataUsage)) * rate);
                dataRecord.appName = (String) app.loadLabel(getActivity().getPackageManager());
                dataRecord.icon = app.loadIcon(getActivity().getPackageManager());
                dataRecord.rate = rate;
                dataRecords.add(dataRecord);
            }
        }
        dataSummaryAdapter.setDataRecordList(dataRecords);
        dataSummaryAdapter.notifyDataSetChanged();
        TrafficRecordUpdateReceiver alarm = new TrafficRecordUpdateReceiver();
        alarm.setAlarm(getContext());
    }

    private List<PointValue> showDeviceDataSummary() {
        dataRecords = DataOperation.getDeviceData(0);

        List<PointValue> values = new ArrayList<PointValue>();

        for (int i = 0; i < dataRecords.size(); i++) {
            DeviceData dataRecord = dataRecords.get(i);
            //values.add(new PointValue(i, i*2));
        }
        return values;
    }

    @OnClick({R.id.computed_rate, R.id.mb_at_kes})
    public void handleClicks(View v) {
        switch (v.getId()) {
            case R.id.computed_rate: {
                enterDataView.setVisibility(View.VISIBLE);
                computedRate.setVisibility(View.GONE);
                break;
            }
            case R.id.mb_at_kes: {
                enterDataView.setVisibility(View.GONE);
                computedRate.setVisibility(View.VISIBLE);
                cost=cost==0?100:cost;
                volume=volume==0?30:volume;
                rate=cost/volume;
                computedRate.setText(getString(R.string.data_usage_rate,rate));
                break;
            }
        }
    }
}
