package com.evanschambers.botboard.datamodels;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Content {
/*
            				"default" : "Send me a message at 571-577-8340",
            				"from" : "+17039260423",
            				"message" : "sending message from console again and again",
            				"timeout" : 60000,
            				"timestamp" : 1450298682512

 */
private static final String TAG = Content.class.getSimpleName();
    private static final String NODE_NAME = "content";
    private String defaultMessage;
    private String message;
    private String from;
    private Long timeout;
    private Long timestamp;

    public Content() {
    }

    public Content(String message1, String defaultMessage1) {
        message = message1;
        defaultMessage = defaultMessage1;
    }

    public static Content createDefaultContent(){
        Content newDefaultContent = new Content();

        return newDefaultContent;
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
                "\"message\":\"" + message + "\", "+
                "\"default\":\"" + defaultMessage + "\", "+
                "\"from\":\"" + from + "\", "+
                "\"timeout\":" + timeout + ", "+
                "\"timestamp\":" + timestamp +
                "}";

        return myJSONValue;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage1) {
        this.defaultMessage = defaultMessage1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message1) {
        this.message = message1;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from1) {
        this.from = from1;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout1) {
        this.timeout = timeout1;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp1) {
        this.timestamp = timestamp1;
    }

}
