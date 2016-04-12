package com.evanschambers.botboard.datamodels;

import java.util.UUID;

/**
 * Created by timvalentine on 3/17/16.
 */
public class SnapShot {
    private static final String TAG = SnapShot.class.getSimpleName();
    private static final String NODE_NAME = "snapshot";
    private String uuid;
    private String status;
    private Today today;
    private UpTime uptime;
    private Yesterday yesterday;

    public SnapShot() {
    }

    public static SnapShot createDefaultSnapshot() {
        SnapShot newDefaultSnapshot = new SnapShot();
        int i = (int) (UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        newDefaultSnapshot.uuid = i + "";

        Content newDefaultContent = Content.createDefaultDashboardContent();
        newDefaultSnapshot.status = "running";
        newDefaultSnapshot.today = Today.createDefaultToday();
        newDefaultSnapshot.uptime = UpTime.createDefaultUpTime();
        newDefaultSnapshot.yesterday = Yesterday.createDefaultYesterday();

        return newDefaultSnapshot;
    }

    public static SnapShot createDefaultDashboardSnapshot() {
        SnapShot newDefaultSnapshot = new SnapShot();
        int i = (int) (UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        newDefaultSnapshot.uuid = i + "";

        newDefaultSnapshot.status = "running";
        newDefaultSnapshot.today = Today.createDefaultToday();
        newDefaultSnapshot.uptime = UpTime.createDefaultUpTime();
        newDefaultSnapshot.yesterday = Yesterday.createDefaultYesterday();

        return newDefaultSnapshot;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        String myJSONValue = getThisJSONValue();

        return NODE_NAME + ":" + myJSONValue;
    }

    public String toJSONValueString() {
        //without the node title - i.e. NODE_NAME + ":" + myJSONValue
        String myJSONValue = getThisJSONValue();

        return myJSONValue;
    }

    private String getThisJSONValue() {
        String myJSONValue =
                "{" +
                        "\"uuid\":\"" + uuid + "\", " +
                        "\"status\":\"" + status + "\", " +
                        "\"today\":\"" + today.toJSONValueString() + "\", " +
                        "\"uptime\":\"" + uptime.toJSONValueString() + "\", " +
                        "\"yesterday\":\"" + yesterday.toJSONValueString() +
                        "}";

        return myJSONValue;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid1) {
        this.uuid = uuid1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String source1) {
        this.status = source1;
    }

    public Today getToday() {
        return today;
    }

    public void setToday(Today today1) {
        this.today = today1;
    }

    public UpTime getUptime() {
        return uptime;
    }

    public void setUptime(UpTime uptime1) {
        this.uptime = uptime1;
    }

    public Yesterday getYesterday() {
        return yesterday;
    }

    public void setYesterday(Yesterday yesterday1) {
        this.yesterday = yesterday1;
    }
}
