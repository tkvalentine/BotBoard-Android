package com.evanschambers.botboard.subcomponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evanschambers.botboard.BotBoardApplication;
import com.evanschambers.botboard.R;
import com.evanschambers.botboard.datamodels.Deck;

import java.util.Collection;
import java.util.Hashtable;

/**
 * Created by timvalentine on 3/28/16.
 */
public class DeckGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater li;
    private int demoItemCount = 7;

    public DeckGridViewAdapter(Context c) {
        mContext = c;
        li = LayoutInflater.from(mContext);
    }

    public int getCount() {
        Hashtable<Integer, Deck> decks = BotBoardApplication.userRecord.getDecks();
        int size = 0;

        if (decks != null) {
            size = decks.size();
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
            View theNewView = li.inflate(R.layout.botboard_deck_table_item_layout, null);
            gridItemView = theNewView;
        } else {
            gridItemView = (View) convertView;
        }

        //get the deck and populate the grid item
        Hashtable<Integer, Deck> decks = BotBoardApplication.userRecord.getDecks();
        Collection<Deck> deckHashtableValues = decks.values();
        Object[] deckArray = deckHashtableValues.toArray();
        Deck theDeck = (Deck) deckArray[position];

        TextView deckName = (TextView) gridItemView.findViewById(R.id.deckTableItemNameTextView);
        deckName.setText(theDeck.getTitle());

        ImageButton deckImage = (ImageButton) gridItemView.findViewById(R.id.deckTableItemImageButton);
        new DownloadImageForImageButtonAsyncTask(deckImage).execute(theDeck.getThumbnail());

        LinearLayout deckItemContainer = (LinearLayout) gridItemView.findViewById(R.id.deckTableItemContainer);
        deckItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to detail activity for deck
                Toast.makeText(DeckGridViewAdapter.this.mContext, "GO TO DECK DETAIL", Toast.LENGTH_LONG).show();
            }
        });

        return gridItemView;
    }
}