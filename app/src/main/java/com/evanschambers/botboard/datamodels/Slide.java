package com.evanschambers.botboard.datamodels;

import java.util.UUID;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Slide {
    private static final String TAG = Slide.class.getSimpleName();
    private static final String NODE_NAME = "slide";
    private String uuid;

    private String type;
    private Boolean showFooter;
    private Content content;
    private Timing timing;
    private Transitions transitions;

    public Slide() {
    }

    public Slide(boolean showFooter1, String type1) {
        showFooter = showFooter1;
        type = type1;
    }

    public static Slide createDefaultSlide(){
        Slide newDefaultSlide = new Slide();
        int i = (int)(UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        newDefaultSlide.uuid = i + "";

        newDefaultSlide.type = "logo";
        newDefaultSlide.showFooter = false;
        newDefaultSlide.content = Content.createDefaultContent();
        newDefaultSlide.timing = Timing.createDefaultTiming();
        newDefaultSlide.transitions = Transitions.createDefaultTransition();

        return newDefaultSlide;
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
                "\"uuid\":\"" + uuid + "\", "+
                "\"showFooter\":\"" + showFooter + "\", "+
                "\"type\":\"" + type + "\", " +
                "\"content\":" + this.content.toJSONValueString() + ", " +
                "\"timing\":" + this.timing.toJSONValueString() + ", " +
                "\"transitions\":" + this.transitions.toJSONValueString() +
                "}";

        return myJSONValue;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid1) {
        this.uuid = uuid1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type1) {
        this.type = type1;
    }

    public Boolean getShowFooter() {
        return showFooter;
    }

    public void setShowFooter(Boolean showFooter1) {
        this.showFooter = showFooter1;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Timing getTiming() {
        return timing;
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }

    public Transitions getTransitions() {
        return transitions;
    }

    public void setTransitions(Transitions transitions) {
        this.transitions = transitions;
    }
}
