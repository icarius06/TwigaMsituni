package com.mozilla.hackathon.twigamsituni.datausage.datausage.persist;

import com.orm.SugarRecord;

/**
 * Created by eugene on 29/04/2016.
 */
public class DeviceData extends SugarRecord {
    public long appTxData;
    public long appRxData;
    public long timeTaken;

    public DeviceData() {

    }

    public DeviceData(long appTxData, long appRxData, long timeTaken) {
        this.appTxData = appTxData;
        this.appRxData = appRxData;
        this.timeTaken = timeTaken;
    }
}
