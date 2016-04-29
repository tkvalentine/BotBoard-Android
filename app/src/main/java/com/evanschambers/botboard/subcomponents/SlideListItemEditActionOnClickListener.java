package com.evanschambers.botboard.subcomponents;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.evanschambers.botboard.BotBoardApplication;
import com.evanschambers.botboard.BotBoardMain;
import com.evanschambers.botboard.R;
import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;
import com.evanschambers.botboard.datamodels.Deck;
import com.evanschambers.botboard.datamodels.Slide;
import com.evanschambers.botboard.datamodels.Timing;
import com.evanschambers.botboard.datamodels.Transitions;

import java.util.Hashtable;

/**
 * Created by timvalentine on 4/27/16.
 */
public class SlideListItemEditActionOnClickListener extends SlideBaseOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        String theTag = (String) v.getTag();
        if (theTag.equals("SLIDE_DETAIL_CONTAINER") ||
                theTag.equals("SLIDE_DETAIL_CONTENT_BUTTON") ||
                theTag.equals("SLIDE_LIST_CREATE_SLIDE_BUTTON")) {
            //go to detail activity for a slide
            Slide theSlide = null;

            if (!theTag.equals("SLIDE_LIST_CREATE_SLIDE_BUTTON")) {
                theSlide = ((Slide) v.getTag(R.integer.TAG_SLIDE_OBJECT));
                SlideListItemEditActionOnClickListener.currentEditSlide = theSlide;

                int slideItemIndex = ((Integer) v.getTag(R.integer.TAG_POSITION_INDEX)).intValue();
                SlideListViewAdapter.lastClickedItemIndex = slideItemIndex;
            } else {
                theSlide = Slide.createDefaultPictureSlide();
                SlideListItemEditActionOnClickListener.currentEditSlide = theSlide;

                saveSlideDetailFields();
                SlideListViewAdapter.lastClickedItemIndex = BotBoardMain.mSlidesBrowserListview.getAdapter().getCount() - 1;
            }

            populateSlideDetailFields();

            adjustVisibleInputFieldsForSlideDetail();

            Handler handler = new Handler();
            Runnable r;
            //put in delayed Runnable so button will flash before action
            r = new Runnable() {
                public void run() {
                    BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL);
                }
            };
            handler.postDelayed(r, 250);
        }
    }

    private void saveSlideDetailFields() {
        Slide editSlide = currentEditSlide;

        //the slide graphic area
        TextView slideNameForSlideDetailGraphicBar =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerSlideName);
        editSlide.setTitle(slideNameForSlideDetailGraphicBar.getText().toString());

// NO TEMPERATURE DATA MEMBER IN SLIDE
//        TextView slideTemperatureForSlideDetailGraphicBar =
//                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerTemperature);
//        slideTemperatureForSlideDetailGraphicBar.setText(barItemSlideTemperature);
//        editSlide.setTemperature(slideTemperatureForSlideDetailGraphicBar.getText());

//THIS SHOULD BE SAVED IN CONTENT
//        //PROBABLY NEED TO MOVE THIS ELSEWHERE
//        if (!editSlide.getType().isEmpty() && editSlide.getType().toLowerCase().equals("picture")) {
//            ImageView slideImage = (ImageView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicImageView);
//            new DownloadImageForImageViewAsyncTask(slideImage).execute(BotBoardFirebaseRecord.convertContentsHashtableToContentsArray(editSlide.getContent())[0].getImageUrl());
//        }

        //the slide data fields
        Spinner slideTypeSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTypeSpinner);
        String theType = slideTypeSpinner.getSelectedItem().toString();
        editSlide.setType(theType);

        TextView slideTitleEditText =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTitleEditText);
        String theTitle = slideTitleEditText.getText().toString();
        editSlide.setTitle(theTitle);

//NOT SURE WHAT THIS IS
//        TextView slideTimeForSlideDetailGraphicBar =
//                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerTime);
//        Timing timing = new Timing();
//        timing.setSlideTime(Long.parseLong(slideTimeForSlideDetailGraphicBar.getText().toString()));
//        editSlide.setTiming(timing);

        //this field is part of the Timing data model
        TextView slideDurationEditText =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideDurationEditText);
        Timing timing = new Timing();
        timing.setSlideTime(Long.parseLong(slideDurationEditText.getText().toString()));
        //this field is part of the Timing data model
        TextView slideTransistionDurationEditText =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionDurationEditText);
        timing.setTransitionTime(Long.parseLong(slideTransistionDurationEditText.getText().toString()));
        editSlide.setTiming(timing);

        //this field is part of the Transistions data model
        Spinner slideTransistionInSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionInSpinner);
        String transIn = slideTransistionInSpinner.getSelectedItem().toString();  //default fade
        Spinner slideTransistionOutSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionOutSpinner);
        String transOut = slideTransistionOutSpinner.getSelectedItem().toString();  //default fade
        Transitions theTransistion = new Transitions();
        theTransistion.setEntry(transIn);
        theTransistion.setExit(transOut);
        editSlide.setTransitions(theTransistion);

        Spinner slideActiveSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideActiveSpinner);
        String active = slideActiveSpinner.getSelectedItem().toString();
        editSlide.setActive(active);

        Spinner slideLogoSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideLogoSpinner);
        String theLogo = slideLogoSpinner.getSelectedItem().toString();
        editSlide.setLogo(theLogo);

        Spinner slideShowFooterSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideShowFooterSpinner);
        String theShowFooter = slideShowFooterSpinner.getSelectedItem().toString();
        boolean showFooter = theShowFooter.equals("yes") ? true : false;
        editSlide.setShowFooter(showFooter);

        //slide list item edit save goes here CORRECT
        //slide create save goes here CORRECT
        Deck[] decks = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(BotBoardApplication.userRecord.getDecks());
        int selectedDeckIndex = ((DeckGridViewAdapter) BotBoardMain.mDecksBrowserGridview.getAdapter()).getLastClickedItemIndex();
        Deck deck = decks[selectedDeckIndex];

        Hashtable<Integer, Slide> slidesHashtable = BotBoardFirebaseRecord.getSlidesHashtableForADeck(selectedDeckIndex, deck);
        slidesHashtable.put(Integer.parseInt(editSlide.getUuid()), editSlide);

        //this will update BotBoardApplication.userRecord
        deck.setSlides(slidesHashtable);

        Slide[] slides = BotBoardFirebaseRecord.getSlidesArrayForADeck(selectedDeckIndex, deck);
        ((SlideListViewAdapter) BotBoardMain.mSlidesBrowserListview.getAdapter()).setSlideArray(slides);

        BotBoardApplication.userRecordIsDirty = true;
    }

    private void populateSlideDetailFields() {
        //the slide graphic area
        String barItemSlideName = currentEditSlide.getTitle();

        TextView slideNameForSlideDetailGraphicBar =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerSlideName);
        slideNameForSlideDetailGraphicBar.setText(barItemSlideName);

        String barItemSlideTime = "What??";
        TextView slideTimeForSlideDetailGraphicBar =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerTime);
        slideTimeForSlideDetailGraphicBar.setText(barItemSlideTime);

        String barItemSlideTemperature = "Who??";
        TextView slideTemperatureForSlideDetailGraphicBar =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicBarContainerTemperature);
        slideTemperatureForSlideDetailGraphicBar.setText(barItemSlideTemperature);

        //PROBABLY NEED TO MOVE THIS ELSEWHERE
        if (!currentEditSlide.getType().isEmpty() && currentEditSlide.getType().toLowerCase().equals("picture")) {
            ImageView slideImage = (ImageView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.mainScreenDetailSlideGraphicImageView);
            new DownloadImageForImageViewAsyncTask(slideImage).execute(BotBoardFirebaseRecord.convertContentsHashtableToContentsArray(currentEditSlide.getContent())[0].getImageUrl());
        }

        //the slide data fields
        Spinner slideTypeSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTypeSpinner);
        int spinnerCount = slideTypeSpinner.getAdapter().getCount();
        slideTypeSpinner.setSelection(1);   //picture default
        for (int i = 0; i < spinnerCount; i++) {
            if (slideTypeSpinner.getAdapter().getItem(i).toString().toLowerCase().equals(currentEditSlide.getType().toLowerCase())) {
                slideTypeSpinner.setSelection(i);
                break;
            }
        }

        TextView slideTitleEditText =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTitleEditText);
        slideTitleEditText.setText(currentEditSlide.getTitle() == null ? "Title?" : currentEditSlide.getTitle());

        //this field is part of the Timing data model
        TextView slideDurationEditText =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideDurationEditText);
        slideDurationEditText.setText(currentEditSlide.getTiming() == null ? "5" : currentEditSlide.getTiming().getSlideTime().toString());

        //this field is part of the Timing data model
        TextView slideTransistionDurationEditText =
                (TextView) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionDurationEditText);
        slideTransistionDurationEditText.setText(currentEditSlide.getTiming() == null ? "2" : currentEditSlide.getTiming().getTransitionTime().toString());

        //this field is part of the Transistions data model
        Spinner slideTransistionInSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionInSpinner);
        int spinnerCount2 = slideTransistionInSpinner.getAdapter().getCount();
        slideTransistionInSpinner.setSelection(0);  //default fade
        for (int i = 0; i < spinnerCount2; i++) {
            if (slideTransistionInSpinner.getAdapter().getItem(i).toString().toLowerCase().equals(currentEditSlide.getTransitions().getEntry().toLowerCase())) {
                slideTransistionInSpinner.setSelection(i);
                break;
            }
        }

        //this field is part of the Transistions data model
        Spinner slideTransistionOutSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionOutSpinner);
        int spinnerCount3 = slideTransistionOutSpinner.getAdapter().getCount();
        slideTransistionOutSpinner.setSelection(0); //default fade
        for (int i = 0; i < spinnerCount3; i++) {
            if (slideTransistionOutSpinner.getAdapter().getItem(i).toString().toLowerCase().equals(currentEditSlide.getTransitions().getExit().toLowerCase())) {
                slideTransistionOutSpinner.setSelection(i);
                break;
            }
        }

        Spinner slideActiveSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideActiveSpinner);
        if (currentEditSlide.getActive() != null && currentEditSlide.getActive().toLowerCase().equals("yes")) {
            slideActiveSpinner.setSelection(0);
        } else {
            slideActiveSpinner.setSelection(1);
        }

        Spinner slideLogoSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideLogoSpinner);
        if (currentEditSlide.getLogo() != null && currentEditSlide.getLogo().toLowerCase().equals("settings")) {
            slideLogoSpinner.setSelection(0);
        } else {
            slideLogoSpinner.setSelection(1);
        }

        Spinner slideShowFooterSpinner =
                (Spinner) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideShowFooterSpinner);
        if (currentEditSlide.getShowFooter() != null && currentEditSlide.getShowFooter()) {
            slideShowFooterSpinner.setSelection(0);
        } else {
            slideShowFooterSpinner.setSelection(1);
        }

        ImageButton content = (ImageButton) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideContextBuildImageButton);
        int selectedSlideIndex = ((SlideListViewAdapter) BotBoardMain.mSlidesBrowserListview.getAdapter()).getLastClickedItemIndex();
        content.setTag(R.integer.TAG_POSITION_INDEX, selectedSlideIndex);
        content.setTag(R.integer.TAG_SLIDE_OBJECT, currentEditSlide);
        content.setTag(R.integer.TAG_SLIDE_CONTENT_INITIATOR, R.integer.TAG_SLIDE_EDIT_DETAIL_CONTENT_BUTTON_);
        content.setOnClickListener(new SlideListItemGoToSlideContentActionOnClickListener());

        Button save = (Button) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.createSlideSaveButton);

        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //slide list item edit save goes here CORRECT
                //slide list create slide save button goes here CORRECT
                //SAVES ALL SLIDE DATA EXCEPT SLIDE CONTENT
                saveSlideDetailFields();

                //go to detail activity for deck
                Handler handler = new Handler();
                Runnable r;
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_LIST);
                    }
                };
                handler.postDelayed(r, 250);
            }
        });

        Button cancel = (Button) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.createSlideCancelButton);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to detail activity for deck
                Handler handler = new Handler();
                Runnable r;
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_LIST);
                    }
                };
                handler.postDelayed(r, 250);
            }
        });
    }

    private void adjustVisibleInputFieldsForSlideDetail() {
        View slideTypeViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTypeListItemContainer);
        Spinner slideTypeSpinner = (Spinner) slideTypeViewContainer.findViewById(R.id.slideTypeSpinner);
        slideTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ;
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String slideTypeText = ((TextView) view).getText().toString();
                currentEditSlide.setType(slideTypeText);
                adjustVisibleInputFieldsForSlideDetailType();
            }
        });

        View slideContextViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideContentListItemContainer);
        ImageButton slideContextImageButton = (ImageButton) slideContextViewContainer.findViewById(R.id.slideContextBuildImageButton);
        slideContextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustVisibleInputFieldsForSlideContent();

                final int contentInitiator = ((Integer) v.getTag(R.integer.TAG_SLIDE_CONTENT_INITIATOR)).intValue();
                populateSlideDetailContentFields(contentInitiator);

                //put in delayed Runnable so button will flash before action
                Handler handler = new Handler();
                Runnable r;
                r = new Runnable() {
                    public void run() {
                        BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL_CONTENT);
                    }
                };
                handler.postDelayed(r, 250);
            }
        });

        adjustVisibleInputFieldsForSlideDetailType();
    }

    private void adjustVisibleInputFieldsForSlideDetailType() {
        String slideType = currentEditSlide.getType();

        //View slideTypeViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTypeListItemContainer);

        View slideTitleViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTitleListItemContainer);
        slideTitleViewContainer.setVisibility(View.GONE);
        View slideDurationViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideDurationListItemContainer);
        slideDurationViewContainer.setVisibility(View.VISIBLE);
        View slideTransistionDurationViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionDurationListItemContainer);
        slideTransistionDurationViewContainer.setVisibility(View.VISIBLE);
        View slideTransitionInViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionInListItemContainer);
        slideTransitionInViewContainer.setVisibility(View.VISIBLE);
        View slideTransitionOutViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideTransistionOutListItemContainer);
        slideTransitionOutViewContainer.setVisibility(View.VISIBLE);
        View slideActiveViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideActiveListItemContainer);
        slideActiveViewContainer.setVisibility(View.GONE);
        View slideLogoViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideLogoListItemContainer);
        slideLogoViewContainer.setVisibility(View.GONE);
        View slideShowFooterViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideShowFooterListItemContainer);
        slideShowFooterViewContainer.setVisibility(View.GONE);
        View slideContextViewContainer = (View) BotBoardMain.mSlidesDetailSubcontentContainer.findViewById(R.id.slideContentListItemContainer);
        slideContextViewContainer.setVisibility(View.VISIBLE);

        if (!slideType.isEmpty() && slideType.toLowerCase().equals("picture")) {
            slideShowFooterViewContainer.setVisibility(View.VISIBLE);
            //and content
        } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("logo")) {
            slideLogoViewContainer.setVisibility(View.VISIBLE);
            slideContextViewContainer.setVisibility(View.GONE);
            //no content
        } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("sms")) {
            slideShowFooterViewContainer.setVisibility(View.VISIBLE);
            //and content
        } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("biopanels")) {
            //and content
        } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("weather")) {
            //and content
        } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("staticvideooverlay")) {
            //and content
        } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("dropin")) {
            slideShowFooterViewContainer.setVisibility(View.VISIBLE);
            //and content
        } else if (!slideType.isEmpty() && slideType.toLowerCase().equals("dashboard")) {
            //and content
        }
    }
}
