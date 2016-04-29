package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Today extends BaseDataModel {
    private static final String TAG = Today.class.getSimpleName();
    private static final String NODE_NAME = "today";
    private Long errorCount;
    private Long ingestCount;
    private Long ingestRate;

    public Today() {
        super();
    }

    public Today(Long ingestCount1, Long errorCount1, Long ingestRate1) {
        super();
        ingestCount = ingestCount1;
        errorCount = errorCount1;
        ingestRate = ingestRate1;
    }

    public static Today createDefaultToday() {
        Today newDefaultToday = new Today();
        newDefaultToday.ingestCount = 10l;
        newDefaultToday.errorCount = 2l;
        newDefaultToday.ingestRate = 5l;

        return newDefaultToday;
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
                "\"ingestRate\":" + ingestRate + ", " +
                "\"ingestCount\":" + ingestCount + ", " +
                "\"errorCount\":" + errorCount +
                "}";

        return myJSONValue;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long slideTime1) {
        this.errorCount = slideTime1;
    }

    public Long getIngestCount() {
        return ingestCount;
    }

    public void setIngestCount(Long transitionTime1) {
        this.ingestCount = transitionTime1;
    }

    public Long getIngestRate() {
        return ingestRate;
    }

    public void setIngestRate(Long ingestRate1) {
        this.ingestRate = ingestRate1;
    }

}
