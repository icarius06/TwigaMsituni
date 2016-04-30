package com.mozilla.hackathon.twigamsituni.datausage.datausage.persist;

import com.mozilla.hackathon.twigamsituni.datausage.DataRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by eugene on 29/04/2016.
 */
public class DataOperation {

    public static List<TrafficRecord> getAppTrafficRecordsByTime(long dateFrom, long dateTo) {
        Select<TrafficRecord> records = Select.from(TrafficRecord.class).whereOr(Condition.prop("timeTaken").gt(dateFrom),
                Condition.prop("timeTaken").eq(dateFrom))
                .and(Condition.prop("timeTaken").lt(dateTo));
        return records.list();
    }

    public static DataRecord getAppTrafficById(int uid, long dateFrom) {
        Select<TrafficRecord> records;
        if(dateFrom>0) {
            records = Select.from(TrafficRecord.class).where(Condition.prop("app_id").eq(uid)).and(Condition.prop("timeTaken").gt(dateFrom)).limit("40");
        }else{
            records = Select.from(TrafficRecord.class).where(Condition.prop("app_id").eq(uid)).orderBy("app_rx_data desc").limit("40");
        }
        DataRecord dataRecord = new DataRecord();
        long sum = 0;
        for (TrafficRecord trafficRecord : records.list()) {
            sum += trafficRecord.appRxData+trafficRecord.appTxData;
        }
        dataRecord.uid = uid;
        dataRecord.usage = String.valueOf(sum/1000);
        return dataRecord;
    }

    public static List<DeviceData> getDeviceData(long dateFrom){
        Select<DeviceData> records;
        if(dateFrom>0) {
            records=Select.from(DeviceData.class).where(Condition.prop("timeTaken").gt(dateFrom)).orderBy("time_taken desc");
        }else{
            records=Select.from(DeviceData.class).orderBy("time_taken desc");
        }
        return records.list();
    }
}
