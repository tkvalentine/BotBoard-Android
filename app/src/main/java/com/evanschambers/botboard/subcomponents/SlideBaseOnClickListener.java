package com.evanschambers.botboard.subcomponents;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.evanschambers.botboard.BotBoardApplication;
import com.evanschambers.botboard.BotBoardMain;
import com.evanschambers.botboard.R;
import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;
import com.evanschambers.botboard.datamodels.Content;
import com.evanschambers.botboard.datamodels.Deck;
import com.evanschambers.botboard.datamodels.Slide;
import com.evanschambers.botboard.datamodels.Today;
import com.evanschambers.botboard.datamodels.UpTime;
import com.evanschambers.botboard.datamodels.Yesterday;

/**
 * Created by timvalentine on 4/27/16.
 */
public class SlideBaseOnClickListener {
    public static Slide currentEditSlide = null;

    protected void populateSlideDetailContentFields(int slideItemContentInitiator) {
        populateSlideContentFields(slideItemContentInitiator);
    }

    protected void populateSlideContentFields(int slideItemContentInitiator) {
        Content theContent = (Content) (currentEditSlide.getContent().values().toArray()[0]);
        String slideType = currentEditSlide.getType();

        //turn them on by slide type
        if (slideType.toLowerCase().equals("picture")) {
            //picture input fields
            TextView slideDetailContentCaptionEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentCaptionEditText);
            slideDetailContentCaptionEditText.setText(theContent == null ? "Caption?" : theContent.getCaption());

            TextView slideDetailContentImageUrlEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlEditText);
            slideDetailContentImageUrlEditText.setText(theContent == null ? "ImageUrl?" : theContent.getImageUrl());
        } else if (slideType.toLowerCase().equals("sms")) {
            //SMS input fields

            TextView slideDetailContentDefaultMessageEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentDefaultMessageEditText);
            slideDetailContentDefaultMessageEditText.setText(theContent == null ? "Default message?" : theContent.getDefaultMessage());

            TextView slideDetailContentMessageEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentMessageEditText);
            slideDetailContentMessageEditText.setText(theContent == null ? "Message?" : theContent.getMessage());

            TextView slideDetailContentPrefixEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentPrefixEditText);
            slideDetailContentPrefixEditText.setText(theContent == null ? "Prefix?" : theContent.getPrefix());

            TextView slideDetailContentTimeoutEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTimeoutEditText);
            slideDetailContentTimeoutEditText.setText(theContent == null ? "Timeout?" : theContent.getTimeout() == null ? null : theContent.getTimeout().toString());

            TextView slideDetailContentFromEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentFromEditText);
            slideDetailContentFromEditText.setText(theContent == null ? "From?" : theContent.getFrom());

            TextView slideDetailContentTimeStampEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTimeStampEditText);
            slideDetailContentTimeStampEditText.setText(theContent == null ? "Timestamp?" : theContent.getTimestamp() == null ? null : theContent.getTimestamp().toString());
        } else if (slideType.toLowerCase().equals("dropin")) {
            //dropin input fields

            TextView slideDetailContentEventEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentEventEditText);
            slideDetailContentEventEditText.setText(theContent == null ? "Event?" : theContent.getEvent());
        } else if (slideType.toLowerCase().equals("biopanels")) {
            //bioPanels input fields

            TextView slideDetailContentBioEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentBioEditText);
            slideDetailContentBioEditText.setText(theContent == null ? "Bio?" : theContent.getBio());

            TextView slideDetailContentNameEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentNameEditText);
            slideDetailContentNameEditText.setText(theContent == null ? "Name?" : theContent.getName());

            TextView slideDetailContentImageUrlEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlEditText);
            slideDetailContentImageUrlEditText.setText(theContent == null ? "ImageUrl?" : theContent.getImageUrl());

            TextView slideDetailContentImageUrlCoverEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlCoverEditText);
            slideDetailContentImageUrlCoverEditText.setText(theContent == null ? "ImageUrlCover?" : theContent.getImageUrlCover());

            TextView slideDetailContentHireYearEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentHireYearEditText);
            slideDetailContentHireYearEditText.setText(theContent == null ? "Hireyear?" : theContent.getHireYear());
        } else if (slideType.toLowerCase().equals("staticvideooverlay")) {
            //staticVideoOverlay input fields

            TextView slideDetailContentVideoUrlEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentVideoUrlEditText);
            slideDetailContentVideoUrlEditText.setText(theContent == null ? "VideoUrl?" : theContent.getVideoUrl());

            TextView slideDetailContentOverlayEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentOverlayEditText);
            slideDetailContentOverlayEditText.setText(theContent == null ? "Overlay?" : theContent.getOverlay());
        } else if (slideType.toLowerCase().equals("weather")) {
            //weather input fields

            TextView slideDetailContentZipEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentZipEditText);
            slideDetailContentZipEditText.setText(theContent == null ? "Zip?" : theContent.getZip());

            TextView slideDetailContentOverlayColorEditText =
                    (TextView) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentOverlayColorEditText);
            slideDetailContentOverlayColorEditText.setText(theContent == null ? "Overlay color?" : theContent.getOverlayColor());
        } else if (slideType.toLowerCase().equals("snapshot")) {
            //SnapShot input fields

            Spinner slideDetailContentStatusSpinner =
                    (Spinner) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentStatusSpinner);
            if (theContent.getStatus() != null && theContent.getStatus().toLowerCase().equals("running")) {
                slideDetailContentStatusSpinner.setSelection(0);
            } else {
                slideDetailContentStatusSpinner.setSelection(1);
            }

            //these currently are not fields but nav buttons
            //possibly to another activity to handle layout and snapshot
            //slideDetailContentTodayContainer.setVisibility(View.VISIBLE);
            //slideDetailContentYesterdayContainer.setVisibility(View.VISIBLE);
            //slideDetailContentUpTimeContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("dashboard")) {
            //dashboard input fields
            ;
            //these currently are not fields but nav buttons
            //possibly to another activity to handle layout and snapshot
        }

        //ALWAYS DO THIS
        final int contentInitiator = slideItemContentInitiator;

        Button save = (Button) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentSaveButton);
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //slide list item edit content button goes here CORRECT
                saveSlideContentDetailFields();

                //go to detail activity for deck
                Handler handler = new Handler();
                Runnable r;
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        //if(contentInitiator == R.integer.TAG_SLIDE_LIST_ITEM_CONTAINER_GOTO_EDIT_BUTTON) {
                        //    BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL);
                        //}else
                        if (contentInitiator == R.integer.TAG_SLIDE_LIST_ITEM_BUTTON_GOTO_CONTENT) {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_LIST);
                        } else if (contentInitiator == R.integer.TAG_SLIDE_EDIT_DETAIL_CONTENT_BUTTON_) {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL);
                        }
                    }
                };
                handler.postDelayed(r, 250);
            }
        });

        Button cancel = (Button) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentCancelButton);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to detail activity for deck
                Handler handler = new Handler();
                Runnable r;
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        //if(contentInitiator == R.integer.TAG_SLIDE_LIST_ITEM_CONTAINER_GOTO_EDIT_BUTTON) {
                        //    BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL);
                        //}else
                        if (contentInitiator == R.integer.TAG_SLIDE_LIST_ITEM_BUTTON_GOTO_CONTENT) {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_LIST);
                        } else if (contentInitiator == R.integer.TAG_SLIDE_EDIT_DETAIL_CONTENT_BUTTON_) {
                            BotBoardMain.mViewAnimator.setDisplayedChild(BotBoardMain.SLIDE_DETAIL);
                        }
                    }
                };
                handler.postDelayed(r, 250);
            }
        });
    }

    protected void saveSlideContentDetailFields() {
        //save the screen data to the slide object
        Content[] contentArray = BotBoardFirebaseRecord.convertSlidesContentHashtableToSlideContentArray(currentEditSlide.getContent());
        Content editContentIn = contentArray[0];
        String slideType = currentEditSlide.getType();

        //CREATE A CLEAN CONTENT AND COPY THE UUID
        Content editContent = new Content();
        editContent.setUuid(editContentIn.getUuid());

        if (slideType.toLowerCase().equals("picture")) {
            /*    picture input fields
            caption-text           ->slideDetailContentCaptionContainer = picture
            imageUrl-text url      ->slideDetailContentImageUrlContainer = picture
            */
            EditText slideDetailContentCaption =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentCaptionEditText);
            String theCaption = slideDetailContentCaption.getText().toString();
            editContent.setCaption(theCaption);

            EditText slideDetailContentImageUrl =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlEditText);
            String theImageUrl = slideDetailContentImageUrl.getText().toString();
            editContent.setImageUrl(theImageUrl);
        } else if (slideType.toLowerCase().equals("sms")) {
            /*   SMS input fields
            default-text           ->slideDetailContentDefaultMessageContainer = SMS
            message-text           ->slideDetailContentMessageContainer = SMS
            prefix-text            ->slideDetailContentPrefixContainer = SMS
            timeout-text number    ->slideDetailContentTimeoutContainer = SMS
            from-text telephone    ->slideDetailContentFromContainer = SMS
            timestamp-datetime     ->slideDetailContentTimeStampContainer = SMS
            */

            EditText slideDetailContentDefaultMessage =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentDefaultMessageEditText);
            String theDefaultMessage = slideDetailContentDefaultMessage.getText().toString();
            editContent.setDefaultMessage(theDefaultMessage);

            EditText slideDetailContentMessage =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentMessageEditText);
            String theMessage = slideDetailContentMessage.getText().toString();
            editContent.setMessage(theMessage);

            EditText slideDetailContentPrefix =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentPrefixEditText);
            String thePrefix = slideDetailContentPrefix.getText().toString();
            editContent.setPrefix(thePrefix);

            EditText slideDetailContentTimeout =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTimeoutEditText);
            try {
                Long theTimeout = Long.parseLong(slideDetailContentTimeout.getText().toString());
                editContent.setTimeout(theTimeout);
            } catch (Exception e) {
                //do nothing
            }

            EditText slideDetailContentFrom =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentFromEditText);
            String theFrom = slideDetailContentFrom.getText().toString();
            editContent.setFrom(theFrom);

            EditText slideDetailContentTimeStamp =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTimeStampEditText);
            try {
                Long theTimeStamp = Long.parseLong(slideDetailContentTimeStamp.getText().toString());
                editContent.setTimestamp(theTimeStamp);
            } catch (Exception e) {
                //do nothing
            }
        } else if (slideType.toLowerCase().equals("dropin")) {
            /*   dropin input fields
            event-text             ->slideDetailContentEventContainer = dropin and dashboard
            */
            EditText slideDetailContentEvent =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentEventEditText);
            String theEvent = slideDetailContentEvent.getText().toString();
            editContent.setEvent(theEvent);
        } else if (slideType.toLowerCase().equals("biopanels")) {
            /*   bioPanels input fields
            bio-text               ->slideDetailContentBioContainer = bioPanels
            name-text              ->slideDetailContentNameContainer = bioPanels
            imageUrlContent-text url->slideDetailContentImageUrlContentContainer = bioPanels
            imageUrlCover-text url ->slideDetailContentImageUrlCoverContainer = bioPanels
            hireYear-text number year->slideDetailContentHireYearContainer = bioPanels
            */
            EditText slideDetailContentBio =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentBioEditText);
            String theBio = slideDetailContentBio.getText().toString();
            editContent.setBio(theBio);

            EditText slideDetailContentName =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentNameEditText);
            String theName = slideDetailContentName.getText().toString();
            editContent.setName(theName);

            EditText slideDetailContentImageUrlContent =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlContentEditText);
            String theImageUrlContent = slideDetailContentImageUrlContent.getText().toString();
            editContent.setImageUrlContent(theImageUrlContent);

            EditText slideDetailContentImageUrlCover =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlCoverEditText);
            String theImageUrlCover = slideDetailContentImageUrlCover.getText().toString();
            editContent.setImageUrlCover(theImageUrlCover);

            EditText slideDetailContentHireYear =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentHireYearEditText);
            String theHireYear = slideDetailContentHireYear.getText().toString();
            editContent.setHireYear(theHireYear);
        } else if (slideType.toLowerCase().equals("staticvideooverlay")) {
            /*   staticVideoOverlay input fields
            videoUrl-text url      ->slideDetailContentVideoUrlContainer = staticVideoOverlay
            overlay-text html      ->slideDetailContentOverlayContainer = staticVideoOverlay
            */
            EditText slideDetailContentVideoUrl =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentVideoUrlEditText);
            String theVideoUrl = slideDetailContentVideoUrl.getText().toString();
            editContent.setVideoUrl(theVideoUrl);

            EditText slideDetailContentOverlay =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentOverlayEditText);
            String theOverlay = slideDetailContentOverlay.getText().toString();
            editContent.setOverlay(theOverlay);
        } else if (slideType.toLowerCase().equals("weather")) {
            /*   weather input fields
            zip-text number        ->slideDetailContentZipContainer = weather
            overlayColor-text color->slideDetailContentOverlayColorContainer = weather
            */
            EditText slideDetailContentZip =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentZipEditText);
            String theZip = slideDetailContentZip.getText().toString();
            editContent.setZip(theZip);

            EditText slideDetailContentOverlayColor =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentOverlayColorEditText);
            String theOverlayColor = slideDetailContentOverlayColor.getText().toString();
            editContent.setOverlayColor(theOverlayColor);
        } else if (slideType.toLowerCase().equals("snapshot")) {
            /*   SnapShot input fields
            status-spinner         ->slideDetailContentStatusContainer = SnapShot
            today-Today            ->slideDetailContentTodayContainer = SnapShot
            yesterday-Yesterday     ->slideDetailContentYesterdayContainer = SnapShot
            uptime-UpTime          ->slideDetailContentUpTimeContainer = SnapShot
            */
            Spinner slideDetailContentStatus =
                    (Spinner) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentStatusSpinner);
            String theStatus = slideDetailContentStatus.getSelectedItem().toString();
            editContent.setStatus(theStatus);

            EditText slideDetailContentTodayIngestRate =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTodayIngestRateEditText);
            String theIngestRate = slideDetailContentTodayIngestRate.getText().toString();
            EditText slideDetailContentTodayIngestCount =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTodayIngestCountEditText);
            String theIngestCount = slideDetailContentTodayIngestCount.getText().toString();
            EditText slideDetailContentTodayErrorCount =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTodayErrorCountEditText);
            String theErrorCount = slideDetailContentTodayErrorCount.getText().toString();
            Today theToday = new Today();
            try {
                theToday.setIngestRate(Long.parseLong(theIngestRate));
            } catch (Exception e) {
                //do nothing
            }
            try {
                theToday.setIngestCount(Long.parseLong(theIngestCount));
            } catch (Exception e) {
                //do nothing
            }
            try {
                theToday.setErrorCount(Long.parseLong(theErrorCount));
            } catch (Exception e) {
                //do nothing
            }
            editContent.setToday(theToday);

            EditText slideDetailContentYesterdayErrorCount =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentYesterdayErrorCountEditText);
            String theErrorCount2 = slideDetailContentYesterdayErrorCount.getText().toString();
            EditText slideDetailContentYesterdayIngestCount =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentYesterdayIngestCountEditText);
            String theIngestCount2 = slideDetailContentYesterdayIngestCount.getText().toString();
            Yesterday theYesterday = new Yesterday();
            try {
                theYesterday.setErrorCount(Long.parseLong(theErrorCount2));
            } catch (Exception e) {
                //do nothing
            }
            try {
                theYesterday.setIngestCount(Long.parseLong(theIngestCount2));
            } catch (Exception e) {
                //do nothing
            }
            editContent.setYesterday(theYesterday);

            EditText slideDetailContentUptimeDownMin =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentUpTimeDownMinutesEditText);
            String theDownMin = slideDetailContentUptimeDownMin.getText().toString();
            EditText slideDetailContentUptimeTotalMin =
                    (EditText) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentUpTimeTotalMinutesEditText);
            String theTotalMin = slideDetailContentUptimeTotalMin.getText().toString();
            UpTime theUpTime = new UpTime();
            try {
                theUpTime.setDownMinutes(Long.parseLong(theDownMin));
            } catch (Exception e) {
                //do nothing
            }
            try {
                theUpTime.setTotalMinutes(Long.parseLong(theTotalMin));
            } catch (Exception e) {
                //do nothing
            }
            editContent.setUptime(theUpTime);
        } else if (slideType.toLowerCase().equals("dashboard")) {
            /*   dashboard input fields - THESE WILL BE SAVED IN ANOTHER EDIT SCREEN
            layout-Layout           ->slideDetailContentLayoutContainer = dashboard
            snapshot-SnapShot       ->slideDetailContentSnapshotContainer = dashboard
            */
        }

        Deck[] decks = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(BotBoardApplication.userRecord.getDecks());
        int selectedDeckIndex = ((DeckGridViewAdapter) BotBoardMain.mDecksBrowserGridview.getAdapter()).getLastClickedItemIndex();
        Deck deck = decks[selectedDeckIndex];

        //Slide[] slides = BotBoardFirebaseRecord.convertSlidesHashtableToSlideArray(deck.getSlides());
        currentEditSlide.getContent().put(Integer.parseInt(editContent.getUuid()), editContent);

        deck.getSlides().put(Integer.parseInt(currentEditSlide.getUuid()), currentEditSlide);
        BotBoardApplication.userRecord.getDecks().put(Integer.parseInt(deck.getUuid()), deck);

        Slide[] newSlides = BotBoardFirebaseRecord.getSlidesArrayForADeck(selectedDeckIndex, deck);
        ((SlideListViewAdapter) BotBoardMain.mSlidesBrowserListview.getAdapter()).setSlideArray(newSlides);

        BotBoardApplication.userRecordIsDirty = true;
    }

    protected void adjustVisibleInputFieldsForSlideContent() {
        String slideType = currentEditSlide.getType();

        /*   SMS input fields
        default-text           ->slideDetailContentDefaultMessageContainer = SMS
        message-text           ->slideDetailContentMessageContainer = SMS
        prefix-text            ->slideDetailContentPrefixContainer = SMS
        timeout-text number    ->slideDetailContentTimeoutContainer = SMS
        from-text telephone    ->slideDetailContentFromContainer = SMS
        timestamp-datetime     ->slideDetailContentTimeStampContainer = SMS
        */
        View slideDetailContentDefaultMessageContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentDefaultMessageContainer);
        slideDetailContentDefaultMessageContainer.setVisibility(View.GONE);

        View slideDetailContentMessageContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentMessageContainer);
        slideDetailContentMessageContainer.setVisibility(View.GONE);

        View slideDetailContentPrefixContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentPrefixContainer);
        slideDetailContentPrefixContainer.setVisibility(View.GONE);

        View slideDetailContentTimeoutContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTimeoutContainer);
        slideDetailContentTimeoutContainer.setVisibility(View.GONE);

        View slideDetailContentFromContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentFromContainer);
        slideDetailContentFromContainer.setVisibility(View.GONE);

        View slideDetailContentTimeStampContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTimeStampContainer);
        slideDetailContentTimeStampContainer.setVisibility(View.GONE);

        /*    picture input fields
        caption-text           ->slideDetailContentCaptionContainer = picture
        imageUrl-text url      ->slideDetailContentImageUrlContainer = picture
        */
        View slideDetailContentCaptionContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentCaptionContainer);
        slideDetailContentCaptionContainer.setVisibility(View.GONE);

        View slideDetailContentImageUrlContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlContainer);
        slideDetailContentImageUrlContainer.setVisibility(View.GONE);

        /*   dropin input fields
        event-text             ->slideDetailContentEventContainer = dropin and dashboard
        */
        View slideDetailContentEventContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentEventContainer);
        slideDetailContentEventContainer.setVisibility(View.GONE);

        /*   bioPanels input fields
        bio-text               ->slideDetailContentBioContainer = bioPanels
        name-text              ->slideDetailContentNameContainer = bioPanels
        imageUrlContent-text url->slideDetailContentImageUrlContentContainer = bioPanels
        imageUrlCover-text url ->slideDetailContentImageUrlCoverContainer = bioPanels
        hireYear-text number year->slideDetailContentHireYearContainer = bioPanels
        */
        View slideDetailContentBioContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentBioContainer);
        slideDetailContentBioContainer.setVisibility(View.GONE);

        View slideDetailContentNameContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentNameContainer);
        slideDetailContentNameContainer.setVisibility(View.GONE);

        View slideDetailContentImageUrlContentContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlContentContainer);
        slideDetailContentImageUrlContentContainer.setVisibility(View.GONE);

        View slideDetailContentImageUrlCoverContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentImageUrlCoverContainer);
        slideDetailContentImageUrlCoverContainer.setVisibility(View.GONE);

        View slideDetailContentHireYearContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentHireYearContainer);
        slideDetailContentHireYearContainer.setVisibility(View.GONE);

        /*   staticVideoOverlay input fields
        videoUrl-text url      ->slideDetailContentVideoUrlContainer = staticVideoOverlay
        overlay-text html      ->slideDetailContentOverlayContainer = staticVideoOverlay
        */
        View slideDetailContentVideoUrlContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentVideoUrlContainer);
        slideDetailContentVideoUrlContainer.setVisibility(View.GONE);

        View slideDetailContentOverlayContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentOverlayContainer);
        slideDetailContentOverlayContainer.setVisibility(View.GONE);

        /*   weather input fields
        zip-text number        ->slideDetailContentZipContainer = weather
        overlayColor-text color->slideDetailContentOverlayColorContainer = weather
        */
        View slideDetailContentZipContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentZipContainer);
        slideDetailContentZipContainer.setVisibility(View.GONE);

        View slideDetailContentOverlayColorContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentOverlayColorContainer);
        slideDetailContentOverlayColorContainer.setVisibility(View.GONE);

        /*   SnapShot input fields
        status-spinner         ->slideDetailContentStatusContainer = SnapShot
        today-Today            ->slideDetailContentTodayContainer = SnapShot
        yesterday-Yesterday     ->slideDetailContentYesterdayContainer = SnapShot
        uptime-UpTime          ->slideDetailContentUpTimeContainer = SnapShot
        */
        View slideDetailContentStatusContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentStatusContainer);
        slideDetailContentStatusContainer.setVisibility(View.GONE);

        View slideDetailContentTodayContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentTodayContainer);
        slideDetailContentTodayContainer.setVisibility(View.GONE);

        View slideDetailContentYesterdayContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentYesterdayContainer);
        slideDetailContentYesterdayContainer.setVisibility(View.GONE);

        View slideDetailContentUpTimeContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentUpTimeContainer);
        slideDetailContentUpTimeContainer.setVisibility(View.GONE);

        /*   dashboard input fields
        layout-Layout           ->slideDetailContentLayoutContainer = dashboard
        snapshot-SnapShot       ->slideDetailContentSnapshotContainer = dashboard
        */
        View slideDetailContentLayoutContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentLayoutContainer);
        slideDetailContentLayoutContainer.setVisibility(View.GONE);

        View slideDetailContentSnapshotContainer =
                (View) BotBoardMain.mSlidesDetailSubcontentContentContainer.findViewById(R.id.slideDetailContentSnapshotContainer);
        slideDetailContentSnapshotContainer.setVisibility(View.GONE);


        //turn them on by slide type
        if (slideType.toLowerCase().equals("picture")) {
            //picture input fields
            slideDetailContentCaptionContainer.setVisibility(View.VISIBLE);
            slideDetailContentImageUrlContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("sms")) {
            //SMS input fields
            slideDetailContentDefaultMessageContainer.setVisibility(View.VISIBLE);
            slideDetailContentMessageContainer.setVisibility(View.VISIBLE);
            slideDetailContentPrefixContainer.setVisibility(View.VISIBLE);
            slideDetailContentTimeoutContainer.setVisibility(View.VISIBLE);
            slideDetailContentFromContainer.setVisibility(View.VISIBLE);
            slideDetailContentTimeStampContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("dropin")) {
            //dropin input fields
            slideDetailContentEventContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("biopanels")) {
            //bioPanels input fields
            slideDetailContentBioContainer.setVisibility(View.VISIBLE);
            slideDetailContentNameContainer.setVisibility(View.VISIBLE);
            slideDetailContentImageUrlContentContainer.setVisibility(View.VISIBLE);
            slideDetailContentImageUrlCoverContainer.setVisibility(View.VISIBLE);
            slideDetailContentHireYearContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("staticvideooverlay")) {
            //staticVideoOverlay input fields
            slideDetailContentVideoUrlContainer.setVisibility(View.VISIBLE);
            slideDetailContentOverlayContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("weather")) {
            //weather input fields
            slideDetailContentZipContainer.setVisibility(View.VISIBLE);
            slideDetailContentOverlayColorContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("snapshot")) {
            //SnapShot input fields
            slideDetailContentStatusContainer.setVisibility(View.VISIBLE);
            slideDetailContentTodayContainer.setVisibility(View.VISIBLE);
            slideDetailContentYesterdayContainer.setVisibility(View.VISIBLE);
            slideDetailContentUpTimeContainer.setVisibility(View.VISIBLE);
        } else if (slideType.toLowerCase().equals("dashboard")) {
            //dashboard input fields
            slideDetailContentLayoutContainer.setVisibility(View.VISIBLE);
            slideDetailContentSnapshotContainer.setVisibility(View.VISIBLE);
        }
    }
}
