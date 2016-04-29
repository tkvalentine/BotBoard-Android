package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Slide extends BaseDataModel {
    private static final String TAG = Slide.class.getSimpleName();
    private static final String NODE_NAME = "slide";

    private String title;
    private String type;
    private Boolean showFooter;
    private Hashtable<Integer, Content> content = new Hashtable<Integer, Content>();
    private Timing timing;
    private Transitions transitions;
    private String active;
    private String logo;

    public Slide() {
        super();
    }

    public Slide(boolean showFooter1, String type1) {
        super();
        showFooter = showFooter1;
        type = type1;
    }

    public static Slide createDefaultPictureSlide() {
        Slide newDefaultSlide = new Slide();

        newDefaultSlide.title = "Picture Slide";
        newDefaultSlide.type = "picture";
        newDefaultSlide.showFooter = true;

        Content newDefaultContent = Content.createDefaultPictureContent();
        newDefaultSlide.content.put(Integer.parseInt(newDefaultContent.getUuid()), newDefaultContent);
        newDefaultSlide.timing = Timing.createDefaultTiming();
        newDefaultSlide.transitions = Transitions.createDefaultTransition();

        return newDefaultSlide;
    }

    public static Slide createDefaultDashboardSlide() {
        Slide newDefaultSlide = new Slide();

        newDefaultSlide.title = "Dashboard Slide";
        newDefaultSlide.type = "dashboard";
//        newDefaultSlide.showFooter = true;

        Content newDefaultContent = Content.createDefaultDashboardContent();
        newDefaultSlide.content.put(Integer.parseInt(newDefaultContent.getUuid()), newDefaultContent);
        newDefaultSlide.timing = Timing.createDefaultTiming();
        newDefaultSlide.transitions = Transitions.createDefaultTransition();

        return newDefaultSlide;
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
                "\"uuid\":\"" + uuid + "\", "+
                "\"title\":\"" + title + "\", " +
                "\"showFooter\":\"" + showFooter + "\", "+
                "\"type\":\"" + type + "\", " +
                "\"timing\":" + this.timing.toJSONValueString() + ", " +
                "\"transitions\":" + this.transitions.toJSONValueString() +
                "\"active\":\"" + active + "\", " +
                "\"logo\":\"" + logo + "\", " +
                "\"content\":" + theContentJSONValue + ", " +
                "}";

        return myJSONValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name1) {
        this.title = name1;
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

    public Hashtable<Integer, Content> getContent() {
        return content;
    }

    public void setContent(Hashtable<Integer, Content> content) {
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

    public String getActive() {
        return active;
    }

    public void setActive(String active1) {
        this.active = active1;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo1) {
        this.logo = logo1;
    }
}
