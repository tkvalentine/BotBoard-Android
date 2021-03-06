package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Timing extends BaseDataModel {
    private static final String TAG = Timing.class.getSimpleName();
    private static final String NODE_NAME = "timing";
    private Long slideTime;
    private Long transitionTime;

    public Timing() {
        super();
    }

    public Timing(Long transitionTime1, Long slideTime1) {
        super();
        transitionTime = transitionTime1;
        slideTime = slideTime1;
    }

    public static Timing createDefaultTiming(){
        Timing newDefaultTiming = new Timing();
        newDefaultTiming.transitionTime = 5000l;
        newDefaultTiming.slideTime = 50000l;

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

    public Long getSlideTime() {
        return slideTime;
    }

    public void setSlideTime(Long slideTime1) {
        this.slideTime = slideTime1;
    }

    public Long getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(Long transitionTime1) {
        this.transitionTime = transitionTime1;
    }
}
