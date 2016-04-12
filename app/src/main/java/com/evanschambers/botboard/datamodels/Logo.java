package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Logo {
    private static final String TAG = Logo.class.getSimpleName();
    private static final String NODE_NAME = "logo";
    private String url;
    private Dimensions dimensions;

    public Logo() {
    }

    public Logo(Dimensions dimensions1, String url1) {
        dimensions = dimensions1;
        url = url1;
    }

    public static Logo createDefaultLogo(){
        Logo newDefaultLogo = new Logo();
        newDefaultLogo.url = "https://s3.amazonaws.com/upboard/logo_ec_white2.png";
        newDefaultLogo.dimensions = new Dimensions(128l, 128l);

        return newDefaultLogo;
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
                "\"dimensions\":" + dimensions.toJSONValueString() + ", " +
                "\"url:\"" + url + "\"" +
                "}";

        return myJSONValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url1) {
        this.url = url1;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions1) {
        this.dimensions = dimensions1;
    }
}
