package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Content {
    private static final String TAG = Content.class.getSimpleName();
    private static final String NODE_NAME = "content";
    private String uuid;
    private String defaultMessage;
    private String message;
    private String from;
    private Long timeout;
    private Long timestamp;
    private String imageUrl;
    private String caption;
    private String event;
    private String prefix;
    private String zip;
    private String overlay;
    private String overlayColor;
    private String videoUrl;
    private String bio;
    private String imageUrlContent;
    private String imageUrlCover;
    private String name;
    private String hireYear;
    private Hashtable<Integer, SnapShot> snapshot = new Hashtable<Integer, SnapShot>();
    private Hashtable<Integer, Layout> layout = new Hashtable<Integer, Layout>();

    public Content() {
    }

    public Content(String message1, String defaultMessage1) {
        message = message1;
        defaultMessage = defaultMessage1;
    }

    public static Content createDefaultPictureContent() {
        //create content for default slide type picture
        Content newDefaultContent = new Content();
        int i = (int) (UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        newDefaultContent.uuid = i + "";
        newDefaultContent.setCaption("Picture Content");
        newDefaultContent.setImageUrl("https://s3.amazonaws.com/upboard/Photos/photo-MEA-Awards.jpg");

        return newDefaultContent;
    }

    public static Content createDefaultDashboardContent() {
        //create content for default slide type picture
        Content newDefaultContent = new Content();
        int i = (int) (UUID.randomUUID().getLeastSignificantBits());
        i = i < 0 ? i * -1 : i;
        newDefaultContent.uuid = i + "";
        newDefaultContent.setEvent("ingestEvent");

        Layout layout = Layout.createDefaultDashboardLayout();
        newDefaultContent.layout.put(Integer.parseInt(layout.getUuid()), layout);

        SnapShot snapshot = SnapShot.createDefaultDashboardSnapshot();
        newDefaultContent.snapshot.put(Integer.parseInt(snapshot.getUuid()), snapshot);

        return newDefaultContent;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        String layoutJSON = getLayoutJSONValue();
        String snapshotJSON = getSnapshotJSONValue();
        String myJSONValue = getThisJSONValue(layoutJSON, snapshotJSON);

        return NODE_NAME + ":" + myJSONValue;
    }

    public String toJSONValueString() {
        String layoutJSON = getLayoutJSONValue();
        String snapshotJSON = getSnapshotJSONValue();
        String myJSONValue = getThisJSONValue(layoutJSON, snapshotJSON);

        return myJSONValue;
    }

    private String getLayoutJSONValue() {
        String layoutJSON = null;

        if (layout != null && layout.size() > 0) {
            layoutJSON = "[";
            Collection<Layout> theLayouts = layout.values();
            Iterator<Layout> iterator = theLayouts.iterator();

            while (iterator.hasNext()) {
                Layout aLayout = iterator.next();
                String aLayoutJSON = aLayout.toJSONValueString();
                layoutJSON += aLayoutJSON;
                if (iterator.hasNext()) {
                    layoutJSON += ", ";
                }
            }
            layoutJSON += "]";
        }

        return layoutJSON;
    }

    private String getSnapshotJSONValue() {
        String snapshotsJSON = null;

        if (snapshot != null && snapshot.size() > 0) {
            snapshotsJSON = "[";
            Collection<SnapShot> theSnapshots = snapshot.values();
            Iterator<SnapShot> iterator = theSnapshots.iterator();

            while (iterator.hasNext()) {
                SnapShot aSnapshot = iterator.next();
                String aSnapshotJSON = aSnapshot.toJSONValueString();
                snapshotsJSON += aSnapshotJSON;
                if (iterator.hasNext()) {
                    snapshotsJSON += ", ";
                }
            }
            snapshotsJSON += "]";
        }

        return snapshotsJSON;
    }

    private String getThisJSONValue(String theLayoutJSONValue, String theSnapshotJSONValue) {
        String myJSONValue =
                "{" +
                "\"message\":\"" + message + "\", "+
                "\"default\":\"" + defaultMessage + "\", "+
                "\"from\":\"" + from + "\", "+
                "\"timeout\":" + timeout + ", "+
                "\"timestamp\":" + timestamp + "\", "+
                "\"imageUrl\":" + imageUrl + "\", "+
                "\"caption\":" + caption + "\", "+
                "\"event\":" + event + "\", "+
                "\"prefix\":" + prefix + "\", "+
                "\"zip\":" + zip + "\", "+
                "\"overlay\":" + overlay + "\", "+
                "\"overlayColor\":" + overlayColor + "\", "+
                "\"videoUrl\":" + videoUrl + "\", "+
                "\"bio\":" + bio + "\", "+
                "\"imageUrlContent\":" + imageUrlContent + "\", "+
                "\"imageUrlCover\":" + imageUrlCover + "\", "+
                "\"name\":" + name + "\", "+
                "\"hireYear\":" + hireYear + "\", "+
                "\"layout\":" + theLayoutJSONValue + "\", "+
                "\"snapshot\":" + theSnapshotJSONValue +
                "}";

        return myJSONValue;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid1) {
        this.uuid = uuid1;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String url1) {
        this.imageUrl = url1;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption1) {
        this.caption = caption1;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event1) {
        this.event = event1;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip1) {
        this.zip = zip1;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix1) {
        this.prefix = prefix1;
    }

    public String getOverlay() {
        return overlay;
    }

    public void setOverlay(String overlay1) {
        this.overlay = overlay1;
    }

    public String getOverlayColor() {
        return overlayColor;
    }

    public void setOverlayColor(String overlayColor1) {
        this.overlayColor = overlayColor1;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl1) {
        this.videoUrl = videoUrl1;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio1) {
        this.bio = bio1;
    }

    public String getImageUrlContent() {
        return imageUrlContent;
    }

    public void setImageUrlContent(String imageUrlContent1) {
        this.imageUrlContent = imageUrlContent1;
    }

    public String getImageUrlCover() {
        return imageUrlCover;
    }

    public void setImageUrlCover(String imageUrlCover1) {
        this.imageUrlCover = imageUrlCover1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name1) {
        this.name = name1;
    }

    public String getHireYear() {
        return hireYear;
    }

    public void setHireYear(String hireYear1) {
        this.hireYear = hireYear1;
    }

    public Hashtable<Integer, Layout> getLayout() {
        return layout;
    }

    public void setLayout(Hashtable<Integer, Layout> layout) {
        this.layout = layout;
    }

    public Hashtable<Integer, SnapShot> getSnapshot() {
        return snapshot;
    }

    public void setSnapShot(Hashtable<Integer, SnapShot> snapshot1) {
        this.snapshot = snapshot1;
    }

}
