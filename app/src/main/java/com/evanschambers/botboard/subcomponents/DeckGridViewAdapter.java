package com.evanschambers.botboard.subcomponents;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evanschambers.botboard.BotBoardApplication;
import com.evanschambers.botboard.BotBoardMain;
import com.evanschambers.botboard.R;
import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;
import com.evanschambers.botboard.datamodels.Deck;
import com.evanschambers.botboard.datamodels.Slide;

import java.util.Hashtable;

/**
 * Created by timvalentine on 3/28/16.
 */
public class DeckGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater li;
    private Deck[] deckArray;

    private static int lastClickedItemIndex = -1;

    public DeckGridViewAdapter(Context c, Deck[] deckArray) {
        mContext = c;
        li = LayoutInflater.from(mContext);
        this.deckArray = deckArray;
    }

    public void setDeckArray(Hashtable<Integer, Deck> newDecks) {
        deckArray = BotBoardFirebaseRecord.convertDecksHashtableToDeckArray(newDecks);
        this.notifyDataSetChanged();
    }

    public void setDeckArray(Deck[] newDecks) {
        deckArray = newDecks;
        this.notifyDataSetChanged();
    }

    public int getLastClickedItemIndex() {
        return lastClickedItemIndex;
    }

    public int getCount() {
        int size = 0;

        if (BotBoardApplication.userRecord != null) {
            if (deckArray != null) {
                size = deckArray.length;
            }
        }

        return size;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            View theNewView = li.inflate(R.layout.botboard_deck_grid_item_layout, null);
            gridItemView = theNewView;
        } else {
            gridItemView = (View) convertView;
        }
        gridItemView.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));

        //get the deck and populate the grid item
        Deck theDeck;
        if (deckArray == null || deckArray.length <= 0) {
            theDeck = Deck.createDefaultDeck();
        } else {
            theDeck = deckArray[position];
        }

        TextView deckName = (TextView) gridItemView.findViewById(R.id.deckTableItemNameTextView);
        deckName.setText(theDeck.getTitle());

        ImageButton deckImage = (ImageButton) gridItemView.findViewById(R.id.deckTableItemImageButton);
        deckImage.setOnClickListener(new DeckGridItemOnClickListener());
        deckImage.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));
        new DownloadImageForImageButtonAsyncTask(deckImage).execute(theDeck.getThumbnail());

        LinearLayout deckItemContainer = (LinearLayout) gridItemView.findViewById(R.id.deckTableItemContainer);
        deckItemContainer.setTag(R.integer.TAG_POSITION_INDEX, new Integer(position));

        return gridItemView;
    }

    private class DeckGridItemOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //get the slides for the deck clicked/selected
            View parentView = (View) v.getParent();
            TextView deckName = (TextView) parentView.findViewById(R.id.deckTableItemNameTextView);
            TextView deckNameForSlides = (TextView) BotBoardMain.mSlidesSubcontentContainer.findViewById(R.id.mainScreenTitleTextViewSlidesPlayDeckName);
            deckNameForSlides.setText(deckName.getText());

            int deckItemIndex = ((Integer) v.getTag(R.integer.TAG_POSITION_INDEX)).intValue();
            lastClickedItemIndex = deckItemIndex;
            Slide[] slides = BotBoardFirebaseRecord.convertSlidesHashtableToSlideArray(DeckGridViewAdapter.this.deckArray[lastClickedItemIndex].getSlides());
            ((SlideListViewAdapter) BotBoardMain.mSlidesBrowserListview.getAdapter()).setSlideArray(slides);

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
    }
}