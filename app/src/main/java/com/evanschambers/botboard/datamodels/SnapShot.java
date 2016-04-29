package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by timvalentine on 3/17/16.
 */
public class SnapShot extends BaseDataModel {
    private static final String TAG = SnapShot.class.getSimpleName();
    private static final String NODE_NAME = "snapshot";
    private Hashtable<Integer, Content> content = new Hashtable<Integer, Content>();

    public SnapShot() {
        super();
    }

    public static SnapShot createDefaultSnapshot() {
        SnapShot newDefaultSnapshot = new SnapShot();

        Content newDefaultContent = Content.createDefaultSnapshotContent();
        newDefaultSnapshot.content.put(Integer.parseInt(newDefaultContent.getUuid()), newDefaultContent);

        return newDefaultSnapshot;
    }

    public static SnapShot createDefaultDashboardSnapshot() {
        SnapShot newDefaultSnapshot = new SnapShot();

        Content newDefaultContent = Content.createDefaultSnapshotContent();
        newDefaultSnapshot.content.put(Integer.parseInt(newDefaultContent.getUuid()), newDefaultContent);

        return newDefaultSnapshot;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        String contentJSON = getContentJSONValue();
        String myJSONValue = getThisJSONValue(contentJSON);

        return NODE_NAME + ":" + myJSONValue;
    }

    public String toJSONValueString() {
        //without the node title - i.e. NODE_NAME + ":" + myJSONValue
        String contentJSON = getContentJSONValue();
        String myJSONValue = getThisJSONValue(contentJSON);

        return myJSONValue;
    }

    private String getContentJSONValue() {
        String contentJSON = "[";

        if (content != null && content.size() > 0) {
            Collection<Content> theContent = content.values();
            Iterator<Content> iterator = theContent.iterator();

            while (iterator.hasNext()) {
                Content aContent = iterator.next();
                String aContentJSON = aContent.toJSONValueString();
                contentJSON += aContentJSON;
                if (iterator.hasNext()) {
                    contentJSON += ", ";
                }
            }
        }
        contentJSON += "]";

        return contentJSON;
    }

    private String getThisJSONValue(String theContentJSONValue) {
        String myJSONValue =
                "{" +
                        "\"uuid\":\"" + uuid + "\", " +
                        "\"content\":" + theContentJSONValue + ", " +
                        "}";

        return myJSONValue;
    }

    public Hashtable<Integer, Content> getContent() {
        return content;
    }

    public void setContent(Hashtable<Integer, Content> content1) {
        this.content = content1;
    }
//
//    public Today getToday() {
//        return today;
//    }
//
//    public void setToday(Today today1) {
//        this.today = today1;
//    }
//
//    public UpTime getUptime() {
//        return uptime;
//    }
//
//    public void setUptime(UpTime uptime1) {
//        this.uptime = uptime1;
//    }
//
//    public Yesterday getYesterday() {
//        return yesterday;
//    }
//
//    public void setYesterday(Yesterday yesterday1) {
//        this.yesterday = yesterday1;
//    }
}
