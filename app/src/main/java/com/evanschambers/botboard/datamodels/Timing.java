package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Timing {
    private static final String TAG = Timing.class.getSimpleName();
    private static final String NODE_NAME = "timing";
    private Long slideTime;
    private Long transitionTime;

    public Timing() {
    }

    public Timing(Long transitionTime1, Long slideTime1) {
        transitionTime = transitionTime1;
        slideTime = slideTime1;
    }

    public static Timing createDefaultTiming(){
        Timing newDefaultTiming = new Timing();
        newDefaultTiming.transitionTime = 10000l;
        newDefaultTiming.slideTime = 2000l;

        return newDefaultTiming;
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

    private String getThisJSONValue(){
        String myJSONValue =
                "{" +
                "\"transitionTime\":" + transitionTime + ", " +
                "\"slideTime:" + slideTime +
                "}";

        return myJSONValue;
    }

    public long getSlideTime() {
        return slideTime;
    }

    public void setSlideTime(long slideTime1) {
        this.slideTime = slideTime1;
    }

    public Long getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(Long transitionTime1) {
        this.transitionTime = transitionTime1;
    }
}
