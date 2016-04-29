package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Transitions extends BaseDataModel {
    private static final String TAG = BotBoardFirebaseRecord.class.getSimpleName();
    private static final String NODE_NAME = "transitions";
    private String entry;
    private String exit;

    public Transitions() {
        super();
    }

    public Transitions(String exit1, String entry1) {
        super();
        exit = exit1;
        entry = entry1;
    }

    public static Transitions createDefaultTransition(){
        Transitions newDefaultTransition = new Transitions();
        newDefaultTransition.entry = "fade";
        newDefaultTransition.exit = "fade";

        return newDefaultTransition;
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
                        "\"exit\":\"" + exit + "\", " +
                        "\"entry\":\"" + entry + "\"" +
                "}";

        return myJSONValue;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry1) {
        this.entry = entry1;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit1) {
        this.exit = exit1;
    }
}
