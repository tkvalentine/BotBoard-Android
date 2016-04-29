package com.evanschambers.botboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ViewAnimator;

import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;
import com.evanschambers.botboard.datamodels.Deck;
import com.evanschambers.botboard.datamodels.Slide;
import com.evanschambers.botboard.subcomponents.DeckGridViewAdapter;
import com.evanschambers.botboard.subcomponents.SlideListItemEditActionOnClickListener;
import com.evanschambers.botboard.subcomponents.SlideListViewAdapter;

import java.util.Hashtable;

/**
 * Created by timvalentine on 3/3/16.
 */
public class BotBoardMain extends Activity implements View.OnClickListener {
    private static final String TAG = BotBoardMain.class.getSimpleName();

    //How often (in ms) we push write updates to Firebase
    //private static final int UPDATE_THROTTLE_DELAY = 40;

    private Menu mActionMenu;
    //menu buttons
    private ImageButton mBrowseDecksImageButton;
    private ImageButton mYourDecksImageButton;
    private ImageButton mSettingsImageButton;
    private ImageButton mLogoutImageButton;
    private ImageButton mUserImageButton;
    private ImageButton mInfomationImageButton;
    //deck panel buttons
    private ImageButton mCreateDeckImageButton;
    //slide panel buttons
    private ImageButton mCreateSlideImageButton;
    private ImageButton mPlaySlideImageButton;
    //slide detail panel buttons
    private ImageButton mDeleteSlideImageButton;

    public static GridView mDecksBrowserGridview;
    public static ListView mSlidesBrowserListview;

    //subpanels for data hierarchy, these are controlled by the ViewAnimator below
    public static View mDecksSubcontentContainer;
    public static View mDecksDetailSubcontentContainer;
    public static View mSlidesSubcontentContainer;
    public static View mSlidesDetailSubcontentContainer;
    public static View mSlidesDetailSubcontentContentContainer;

    public static ViewAnimator mViewAnimator;
    public static boolean isActivityInCreation = true;
    public static boolean isActivityIntializing = true;

    public static int DECK_GRID = 0;
    public static int DECK_DETAIL = 1;
    public static int SLIDE_LIST = 2;
    public static int SLIDE_DETAIL = 3;
    public static int SLIDE_DETAIL_CONTENT = 4;
    public static int SLIDE_DETAIL_CONTENT_LAYOUT = 5;
    public static int SLIDE_DETAIL_CONTENT_SNAPSHOT = 6;

    public BotBoardMain() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, ">>>onCreate() : ENTER");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.botboard_main_layout);

        //menu buttons
        mBrowseDecksImageButton = (ImageButton) findViewById(R.id.mainScreenBrowseDecksButton);
        mBrowseDecksImageButton.setOnClickListener(this);

        mYourDecksImageButton = (ImageButton) findViewById(R.id.mainScreenYourDecksButton);
        mYourDecksImageButton.setOnClickListener(this);

        mSettingsImageButton = (ImageButton) findViewById(R.id.mainScreenSettingsButton);
        mSettingsImageButton.setOnClickListener(this);

        mLogoutImageButton = (ImageButton) findViewById(R.id.mainScreenLogoutButton);
        mLogoutImageButton.setOnClickListener(this);

        mUserImageButton = (ImageButton) findViewById(R.id.mainScreenUserButton);
        mUserImageButton.setOnClickListener(this);

        mInfomationImageButton = (ImageButton) findViewById(R.id.mainScreenPlayInfoButton);
        mInfomationImageButton.setOnClickListener(this);

        //deck panel buttons
        mCreateDeckImageButton = (ImageButton) findViewById(R.id.mainScreenCreateDeckButton);
        mCreateDeckImageButton.setOnClickListener(new DeckGridViewAdapter.DeckGridItemCreateOnClickListener());

        //slide panel buttons
        mCreateSlideImageButton = (ImageButton) findViewById(R.id.mainScreenCreateSlideButton);
        mCreateSlideImageButton.setOnClickListener(new SlideListItemEditActionOnClickListener());

        mPlaySlideImageButton = (ImageButton) findViewById(R.id.mainScreenPlaySlideButton);
        mPlaySlideImageButton.setOnClickListener(this);

        //slide detail panel buttons
        mDeleteSlideImageButton = (ImageButton) findViewById(R.id.mainScreenDeleteSlideDetailButton);
        mDeleteSlideImageButton.setOnClickListener(this);

        //by virtue of logging in we either have a default user record or retrieved record data
        //view subcontent
        Deck[] deckArray = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(BotBoardApplication.userRecord.getDecks());

        mDecksBrowserGridview = (GridView) findViewById(R.id.mainScreenDecksGridView);
        mDecksBrowserGridview.setAdapter(new DeckGridViewAdapter(this, deckArray));

        //get the slides later when a deck is selected for view/edit
        //by virtue of logging in we either have a default user record or retrieved record data
        mSlidesBrowserListview = (ListView) findViewById(R.id.mainScreenSlidesListView);
        mSlidesBrowserListview.setAdapter(new SlideListViewAdapter(this, new Slide[]{}));

        mDecksSubcontentContainer = (View) findViewById(R.id.mainScreenDecksSubcontentContainer);//decksSubcontent);
        mDecksDetailSubcontentContainer = (View) findViewById(R.id.mainScreenDeckDetailSubcontentContainer);//decksDetailSubcontent);
        mSlidesSubcontentContainer = (View) findViewById(R.id.mainScreenSlidesSubcontentContainer);//slidesSubcontent);
        mSlidesDetailSubcontentContainer = (View) findViewById(R.id.mainScreenSlidesDetailSubcontentContainer);//slidesDetailSubcontent);
        mSlidesDetailSubcontentContentContainer = (View) findViewById(R.id.mainScreenSlidesDetailContentSubcontentContainer);//slidesDetailSubcontentContent);

        mViewAnimator = (ViewAnimator) findViewById(R.id.mainSubcontentScreenViewAnimator);

        final Animation inAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);//fade_in);//
        final Animation outAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);//.fade_out);//
        inAnim.setDuration(1000l);
        outAnim.setDuration(500l);
        mViewAnimator.setInAnimation(inAnim);
        mViewAnimator.setOutAnimation(outAnim);

        isActivityInCreation = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.botboard_main_menu, menu);
        mActionMenu = menu;
        return true;
    }

    public boolean signOut(MenuItem item) {
        finishWithReturnResult(true);
        return true;
    }

    private void finishWithReturnResult(boolean result) {
        Intent signOutIntent = new Intent(this, BotBoardLogin.class);
        signOutIntent.putExtra("SIGNOUT", result);
        setResult(RESULT_OK, signOutIntent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        finishWithReturnResult(true);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        Handler handler = new Handler();
        Runnable r;

        switch (viewId) {
            //main menu buttons
            case R.id.mainScreenBrowseDecksButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        mViewAnimator.setDisplayedChild(DECK_GRID);
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenBrowseDecksButton");
                break;
            case R.id.mainScreenYourDecksButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        mViewAnimator.setDisplayedChild(DECK_GRID);
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenYourDecksButton");
                break;
            case R.id.mainScreenSettingsButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        showSettingsDetail();
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenSettingsButton");
                break;
            case R.id.mainScreenLogoutButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        finishWithReturnResult(true);
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenLogoutButton");
                break;
            case R.id.mainScreenUserButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        showUserDetail();
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenUserButton");
                break;
            case R.id.mainScreenPlayInfoButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        showInfoDetail();
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenPlayInfoButton");
                break;
            //deck panel buttons
            //case R.id.mainScreenCreateDeckButton:     //HANDLED WITH CUSTOM ONCLICKLISTENER
            //slide panel buttons
            //case R.id.mainScreenCreateSlideButton:     //HANDLED WITH CUSTOM ONCLICKLISTENER
            case R.id.mainScreenPlaySlideButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        playSlide();
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenCreateSlideButton");
                break;
            //slide detail panel buttons
            case R.id.mainScreenDeleteSlideDetailButton:
                //put in delayed Runnable so button will flash before action
                r = new Runnable() {
                    public void run() {
                        deleteSlide();
                    }
                };
                handler.postDelayed(r, 250);

                Log.i(TAG, ">>>onClick() : mainScreenDeleteSlideDetailButton");
                break;

            default:
                break;
        }
    }

    private void showUserDetail() {
        Log.i(TAG, ">>>showUserDetail() : ENTER");

    }

    private void showInfoDetail() {
        Log.i(TAG, ">>>showInfoDetail() : ENTER");

    }

    private void showSettingsDetail() {
        Log.i(TAG, ">>>showSettingsDetail() : ENTER");

    }

    private void playSlide() {
        Log.i(TAG, ">>>playSlide() : ENTER");

    }

    private void deleteSlide() {
        Log.i(TAG, ">>>deleteSlide() : ENTER");

        Deck[] decks = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(BotBoardApplication.userRecord.getDecks());
        int selectedDeckIndex = ((DeckGridViewAdapter) mDecksBrowserGridview.getAdapter()).getLastClickedItemIndex();
        Deck deck = decks[selectedDeckIndex];

        Hashtable<Integer, Slide> slidesHashtable = BotBoardFirebaseRecord.getSlidesHashtableForADeck(selectedDeckIndex, deck);

        int selectedSlideIndex = ((SlideListViewAdapter) mSlidesBrowserListview.getAdapter()).getLastClickedItemIndex();
        Slide[] slides = BotBoardFirebaseRecord.convertSlidesHashtableToSlideArray(slidesHashtable);
        Slide slide = slides[selectedSlideIndex];
        slidesHashtable.remove(Integer.parseInt(slide.getUuid()));

        //this will update BotBoardApplication.userRecord
        deck.setSlides(slidesHashtable);

        Slide[] newSlides = BotBoardFirebaseRecord.getSlidesArrayForADeck(selectedDeckIndex, deck);
        ((SlideListViewAdapter) mSlidesBrowserListview.getAdapter()).setSlideArray(newSlides);

        BotBoardApplication.userRecordIsDirty = true;

        //go back to slide list view
        mViewAnimator.setDisplayedChild(SLIDE_LIST);
    }
}
