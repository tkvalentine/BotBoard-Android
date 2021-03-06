package com.evanschambers.botboard.datamodels;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by timvalentine on 3/17/16.
 */
public class BotBoardFirebaseRecord {

    private static final String TAG = BotBoardFirebaseRecord.class.getSimpleName();
    private static final String NODE_NAME = "users";
    private String type;
    private String name;
    private String email;

    private String userAuthId;

    private Hashtable<Integer, Deck> decks = new Hashtable<Integer, Deck>();

    public BotBoardFirebaseRecord() {
    }

    public BotBoardFirebaseRecord(String userAuthId1, String name1, String email1, String type1) {
        userAuthId = userAuthId1;
        name = name1;
        email = email1;
        type = type1;
    }

    public static BotBoardFirebaseRecord createDefaultUserRecord(String userAuthId1, String name1, String email1, String type1){
        BotBoardFirebaseRecord newDefaultRecord = new BotBoardFirebaseRecord(userAuthId1, name1, email1, type1);
        Deck newDefaultDeck = Deck.createDefaultDeck();
        newDefaultRecord.decks = new Hashtable<Integer, Deck>();
        newDefaultRecord.decks.put(Integer.parseInt(newDefaultDeck.getUuid()), newDefaultDeck);

        return newDefaultRecord;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        String decksJSON = getDecksJSONValue();
        String myJSONValue = getThisJSONValue(decksJSON);

        return NODE_NAME + ":" + myJSONValue;
    }

    public String toJSONValueString() {
        //without the node name - i.e. NODE_NAME + ":" + myJSONValue
        String decksJSON = getDecksJSONValue();
        String myJSONValue = getThisJSONValue(decksJSON);

        return myJSONValue;
    }

    private String getDecksJSONValue(){
        String decksJSON = "[";

        if(decks != null && decks.size() > 0) {
            Collection<Deck> theDecks = decks.values();
            Iterator<Deck> iterator = theDecks.iterator();

            while (iterator.hasNext()) {
                Deck aDeck = iterator.next();
                String aDeckJSON = aDeck.toJSONValueString();
                decksJSON += aDeckJSON;
                if (iterator.hasNext()) {
                    decksJSON += ", ";
                }
            }
        }
        decksJSON += "]";

        return decksJSON;
    }

    private String getThisJSONValue(String theDecksJSONValue){
        String myJSONValue =
                "{" +
                    "\"name\":\"" + name + "\", "+
                    "\"email\":\"" + email + "\", "+
                    "\"type\":\"" + type + "\", "+
                    "\"decks\":" + theDecksJSONValue +
                "}";

        return myJSONValue;
    }

    public String getUserAuthId() {
        return userAuthId;
    }

    public void setUserAuthId(String userAuthId2) {
        this.userAuthId = userAuthId2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Hashtable<Integer, Deck> getDecks() {
        return decks;
    }

    public void setDecks(Hashtable<Integer, Deck> decks) {
        this.decks = decks;
    }

    public static Deck[] convertDecksHashtableToDeckArray(Hashtable<Integer, Deck> decks) {
        Deck[] deckArray = null;
        if (decks == null || decks.size() <= 0) {
            deckArray = new Deck[]{};
        } else {
            Collection<Deck> deckHashtableValues = decks.values();
            Object[] deckObjectArray = deckHashtableValues.toArray();
            deckArray = new Deck[deckObjectArray.length];
            for(int i = 0; i < deckObjectArray.length; i++){
                deckArray[i] = (Deck)deckObjectArray[i];
            }
        }
        return deckArray;
    }

    public static Content[] convertSlidesContentHashtableToSlideContentArray(Hashtable<Integer, Content> contents) {
        Content[] contentArray = null;
        if (contents == null || contents.size() <= 0) {
            contentArray = new Content[]{};
        } else {
            Collection<Content> contentHashtableValues = contents.values();
            Object[] contentObjectArray = contentHashtableValues.toArray();
            contentArray = new Content[contentObjectArray.length];
            for(int i = 0; i < contentObjectArray.length; i++){
                contentArray[i] = (Content)contentObjectArray[i];
            }
        }
        return contentArray;
    }

    public static Slide[] convertSlidesHashtableToSlideArray(Hashtable<Integer, Slide> slides) {
        Slide[] slideArray = null;
        if (slides == null || slides.size() <= 0) {
            slideArray = new Slide[]{};
        } else {
            Collection<Slide> slidesHashtableValues = slides.values();
            Object[] slideObjectArray = slidesHashtableValues.toArray();
            slideArray = new Slide[slideObjectArray.length];
            for(int i = 0; i < slideObjectArray.length; i++){
                slideArray[i] = (Slide)slideObjectArray[i];
            }
        }
        return slideArray;
    }

    public static Slide[] getSlidesArrayForADeck(int selectedDeckIndex, Deck deck){
        Slide[] slides = null;

        slides = BotBoardFirebaseRecord.convertSlidesHashtableToSlideArray(deck.getSlides());
        return slides;
    }

    public static Hashtable<Integer, Slide> getSlidesHashtableForADeck(int selectedDeckIndex, Deck deck){
        Hashtable<Integer, Slide> slides = null;

        slides = deck.getSlides();
        return slides;
    }

    public static Content[] convertContentsHashtableToContentsArray(Hashtable<Integer, Content> contents) {
        Content[] contentArray = null;
        if (contents == null || contents.size() <= 0) {
            contentArray = new Content[]{};
        } else {
            Collection<Content> contentHashtableValues = contents.values();
            Object[] contentObjectArray = contentHashtableValues.toArray();
            contentArray = new Content[contentObjectArray.length];
            for (int i = 0; i < contentObjectArray.length; i++) {
                contentArray[i] = (Content) contentObjectArray[i];
            }
        }
        return contentArray;
    }

    public static Content[] getContentArrayForASlide(int selectedSlideIndex, Slide slide) {
        Content[] content = null;

        content = BotBoardFirebaseRecord.convertContentsHashtableToContentsArray(slide.getContent());
        return content;
    }
}
