package com.mozilla.hackathon.twigamsituni.datausage.datausage.persist;

import com.orm.SugarRecord;

/**
 * Created by eugene on 29/04/2016.
 */
public class TrafficRecord extends SugarRecord{

    int appId;
    long appTxData;
    long appRxData;
    long timeTaken;

    public TrafficRecord() {

    }

    public TrafficRecord(int appId, long appTxData, long appRxData, long timeTaken) {
        this.appId = appId;
        this.appTxData = appTxData;
        this.appRxData = appRxData;
        this.timeTaken = timeTaken;
    }
}
