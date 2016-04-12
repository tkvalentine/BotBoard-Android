package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Yesterday {
    private static final String TAG = Yesterday.class.getSimpleName();
    private static final String NODE_NAME = "yesterday";
    private Long errorCount;
    private Long ingestCount;

    public Yesterday() {
    }

    public Yesterday(Long ingestCount1, Long errorCount1) {
        ingestCount = ingestCount1;
        errorCount = errorCount1;
    }

    public static Yesterday createDefaultYesterday() {
        Yesterday newDefaultYesterday = new Yesterday();
        newDefaultYesterday.ingestCount = 10l;
        newDefaultYesterday.errorCount = 2l;

        return newDefaultYesterday;
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
                        "\"ingestCount\":" + ingestCount + ", " +
                        "\"errorCount:" + errorCount +
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
}
