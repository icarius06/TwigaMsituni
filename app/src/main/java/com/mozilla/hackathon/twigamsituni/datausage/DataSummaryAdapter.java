package com.mozilla.hackathon.twigamsituni.datausage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mozilla.hackathon.twigamsituni.R;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eugene on 28/04/2016.
 */

public class DataSummaryAdapter extends RecyclerView.Adapter<DataSummaryAdapter.SuperViewHolder> {

    private static final int VIEW_TYPE_HEADER_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_TRANSACTION_ITEM = 1;

    private List<DataRecord> dataRecordList = new LinkedList();

    private Context context;
    private boolean isTotalCostMode = true;


    public DataSummaryAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    public abstract static class SuperViewHolder extends RecyclerView.ViewHolder {
        public SuperViewHolder(View v) {
            super(v);
        }
    }

    public static class DataSummaryViewHolder extends SuperViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public TextView costPerMb;
        public TextView data;

        public DataSummaryViewHolder(View v) {
            super(v);
            data = (TextView) v.findViewById(R.id.data);
            costPerMb = (TextView) v.findViewById(R.id.costPerMb);
            appName = (TextView) v.findViewById(R.id.appName);
            appIcon = (ImageView) v.findViewById(R.id.appIcon);

        }
    }


    public static class HeaderViewHolder extends SuperViewHolder {

        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_HEADER_PLACEHOLDER;
        } else {
            return VIEW_TYPE_TRANSACTION_ITEM;
        }
    }


    @Override
    public DataSummaryAdapter.SuperViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {


        if (viewType == VIEW_TYPE_HEADER_PLACEHOLDER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.traffic_stats_header, parent, false);

            HeaderViewHolder vh = new HeaderViewHolder(v);
            return vh;
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_stats_item, parent, false);

        DataSummaryViewHolder vh = new DataSummaryViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {

        // header placeholder
        if (position == 0) {
            return;
        }

        final DataSummaryViewHolder viewHolder = (DataSummaryViewHolder) holder;

        final DataRecord dataRecord = dataRecordList.get(position - 1);


        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        if (isTotalCostMode) {
            viewHolder.costPerMb.setText(context.getString(R.string.cost_per_mb, formatter.format(Float.parseFloat(dataRecord.costPerMb))));
        } else {
            viewHolder.costPerMb.setText(context.getString(R.string.cost_per_mb, formatter.format(dataRecord.rate)));
        }

        viewHolder.data.setText(context.getString(R.string.data_usage, dataRecord.usage));

        viewHolder.appName.setText(dataRecord.appName);

        viewHolder.appIcon.setImageDrawable(dataRecord.icon);

        viewHolder.costPerMb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTotalCostMode) {
                    isTotalCostMode = false;
                } else {
                    isTotalCostMode = true;
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (dataRecordList == null)
            return 0;
        return dataRecordList.size() + 1;
    }

    public void setDataRecordList(List<DataRecord> dataRecordList) {
        this.dataRecordList = dataRecordList;
    }
}