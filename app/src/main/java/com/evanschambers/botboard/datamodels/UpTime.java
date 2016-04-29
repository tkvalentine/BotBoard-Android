package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class UpTime extends BaseDataModel {
    private static final String TAG = UpTime.class.getSimpleName();
    private static final String NODE_NAME = "uptime";
    private Long downMinutes;
    private Long totalMinutes;

    public UpTime() {
        super();
    }

    public UpTime(Long ingestCount1, Long errorCount1) {
        super();
        totalMinutes = ingestCount1;
        downMinutes = errorCount1;
    }

    public static UpTime createDefaultUpTime() {
        UpTime newDefaultUptime = new UpTime();
        newDefaultUptime.totalMinutes = 10569104l;
        newDefaultUptime.downMinutes = 17l;

        return newDefaultUptime;
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
        String myJSONValue = getThisJSONValue();

        return myJSONValue;
    }

    private String getThisJSONValue() {
        String myJSONValue =
                "{" +
                "\"totalMinutes\":" + totalMinutes + ", " +
                "\"downMinutes\":" + downMinutes +
                "}";

        return myJSONValue;
    }

    public long getDownMinutes() {
        return downMinutes;
    }

    public void setDownMinutes(long downMinutes1) {
        this.downMinutes = downMinutes1;
    }

    public Long getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Long totalMinutes1) {
        this.totalMinutes = totalMinutes1;
    }
}
