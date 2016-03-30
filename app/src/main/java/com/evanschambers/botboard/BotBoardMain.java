package com.evanschambers.botboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.evanschambers.botboard.subcomponents.DeckGridViewAdapter;

/**
 * Created by timvalentine on 3/3/16.
 */
public class BotBoardMain extends Activity implements View.OnClickListener {
    private static final String TAG = BotBoardMain.class.getSimpleName();

    //How often (in ms) we push write updates to Firebase
    //private static final int UPDATE_THROTTLE_DELAY = 40;

    private Menu mActionMenu;
    private ImageButton mBrowseDecksImageButton;
    private ImageButton mCreateDeckImageButton;
    private ImageButton mYourDecksImageButton;
    private ImageButton mSettingsImageButton;
    private ImageButton mLogoutImageButton;
    private ImageButton mUserImageButton;
    private ImageButton mInfomationImageButton;
    GridView mDecksBrowserGridview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, ">>>onCreate() : ENTER");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.botboard_main_layout);

        mBrowseDecksImageButton = (ImageButton) findViewById(R.id.mainScreenBrowseDecksButton);
        mBrowseDecksImageButton.setOnClickListener(this);

        mCreateDeckImageButton = (ImageButton) findViewById(R.id.mainScreenCreateDeckButton);
        mCreateDeckImageButton.setOnClickListener(this);

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

        mDecksBrowserGridview = (GridView) findViewById(R.id.mainScreenDecksGridView);
        mDecksBrowserGridview.setAdapter(new DeckGridViewAdapter(this));
        mDecksBrowserGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(BotBoardMain.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
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

        switch (viewId) {
            case R.id.mainScreenBrowseDecksButton:
                Log.i(TAG, ">>>onClick() : mainScreenBrowseDecksButton");
                break;
            case R.id.mainScreenCreateDeckButton:
                Log.i(TAG, ">>>onClick() : mainScreenCreateDeckButton");
                break;
            case R.id.mainScreenYourDecksButton:
                Log.i(TAG, ">>>onClick() : mainScreenYourDecksButton");
                break;
            case R.id.mainScreenSettingsButton:
                Log.i(TAG, ">>>onClick() : mainScreenSettingsButton");
                break;
            case R.id.mainScreenLogoutButton:
                Log.i(TAG, ">>>onClick() : mainScreenLogoutButton");
                break;
            case R.id.mainScreenUserButton:
                Log.i(TAG, ">>>onClick() : mainScreenUserButton");
                break;
            case R.id.mainScreenPlayInfoButton:
                Log.i(TAG, ">>>onClick() : mainScreenPlayInfoButton");
                break;
            default:
                break;
        }
    }
}
