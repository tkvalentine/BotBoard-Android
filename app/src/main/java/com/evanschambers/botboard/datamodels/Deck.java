package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by timvalentine on 3/17/16.
 */
public class Deck extends BaseDataModel {

    private static final String TAG = Deck.class.getSimpleName();
    private static final String NODE_NAME = "deck";

    private Boolean active;
    private String description;

    private String thumbnail;
    private String title;

    private Long createdDate;
    private Long updatedDate;
    private String logo;
//    private Logo logo;
    private Hashtable<Integer, Slide> slides = new Hashtable<Integer, Slide>();

    public Deck() {
        super();
    }

    public static Deck createDefaultDeck(){
        Deck newDefaultDeck = new Deck();

        newDefaultDeck.active = false;
        newDefaultDeck.description = "Alternative company slide deck for Evans and Chambers";

        newDefaultDeck.thumbnail = "https://s3.amazonaws.com/upboard/logo_ec_white2.png";
        newDefaultDeck.title = "Evans and Chambers";

        newDefaultDeck.createdDate = System.currentTimeMillis();
        newDefaultDeck.updatedDate = newDefaultDeck.createdDate;
//        newDefaultDeck.logo = Logo.createDefaultLogo();
        newDefaultDeck.logo = "settings";

        newDefaultDeck.slides = new Hashtable<Integer, Slide>();

        Slide newDefaultSlide1 = Slide.createDefaultPictureSlide();
        newDefaultDeck.slides.put(Integer.parseInt(newDefaultSlide1.getUuid()), newDefaultSlide1);

        Slide newDefaultSlide2 = Slide.createDefaultDashboardSlide();
        newDefaultDeck.slides.put(Integer.parseInt(newDefaultSlide2.getUuid()), newDefaultSlide2);

        return newDefaultDeck;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        String slidesJSON = getSlidesJSONValue();
        String myJSONValue = getThisJSONValue(slidesJSON);

        return NODE_NAME + ":" + myJSONValue;
    }

    public String toJSONValueString() {
        //without the node name - i.e. NODE_NAME + ":" + myJSONValue
        String slidesJSON = getSlidesJSONValue();
        String myJSONValue = getThisJSONValue(slidesJSON);

        return myJSONValue;
    }

    private String getSlidesJSONValue(){
        String slidesJSON = "[";

        if(slides != null && slides.size() > 0) {
            Collection<Slide> theSlides = slides.values();
            Iterator<Slide> iterator = theSlides.iterator();

            while (iterator.hasNext()) {
                Slide aSlide = iterator.next();
                String aSlideJSON = aSlide.toJSONValueString();
                slidesJSON += aSlideJSON;
                if (iterator.hasNext()) {
                    slidesJSON += ", ";
                }
            }
        }
        slidesJSON += "]";

        return slidesJSON;
    }

    private String getThisJSONValue(String theSlidesJSONValue){
        String myJSONValue =
            "{" +
            "\"uuid\":\"" + uuid + "\", "+
            "\"active\":" + active + ", "+
            "\"description\":\"" + description + "\", "+
            "\"thumbnail\":" +  thumbnail + ", "+
            "\"title\":\"" + title + "\", "+
            "\"updatedDate\":" + updatedDate + ", "+
            "\"createdDate\":" + createdDate + ", "+
//            "\"logo\":" + logo.toJSONValueString() + ", "+
            "\"logo\":" + logo + ", "+
            "\"slides\":" + theSlidesJSONValue +
            "}";

        return myJSONValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description1) {
        this.description = description1;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active1) {
        this.active = active1;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }
//
//    public Logo getLogo() {
//        return logo;
//    }
//
//    public void setLogo(Logo logo1) {
//        this.logo = logo1;
//    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo1) {
        this.logo = logo1;
    }

    public Hashtable<Integer, Slide> getSlides(){
        return slides;
    }

    public void setSlides(Hashtable<Integer, Slide> slides1){
        this.slides = slides1;
    }
}
