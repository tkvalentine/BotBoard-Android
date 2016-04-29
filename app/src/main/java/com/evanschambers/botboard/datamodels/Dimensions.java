package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Dimensions extends BaseDataModel {
    private static final String TAG = Dimensions.class.getSimpleName();
    private static final String NODE_NAME = "dimensions";
    private Long height = 128l;
    private Long width = 128l;

    public Dimensions() {
        super();
    }

    public Dimensions(Long width1, Long height1) {
        super();
        width = width1;
        height = height1;
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
                "\"width\":" + width + ", " +
                "\"height:\"" + height + "" +
                "}";

        return myJSONValue;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height1) {
        this.height = height1;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width1) {
        this.width = width1;
    }
}
