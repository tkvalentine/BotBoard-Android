package com.evanschambers.botboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by timvalentine on 3/3/16.
 */
public class BotBoardMain2 extends Activity {
    private static final String TAG = BotBoardMain2.class.getSimpleName();

    // How often (in ms) we push write updates to Firebase
    private static final int UPDATE_THROTTLE_DELAY = 40;

    private Menu mActionMenu;

    //get a singleton reference to a Firebase root user endpoint
    private Query myUserDataFirebaseQuery;
    public String myAuthToken;
    public AuthData myAuthData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, ">>>onCreate() : ENTER");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.botboard_main_layout);

        //initialize Firebase
        Firebase.setAndroidContext(this);

        //===GET FIREBASE REFERENCE
        //get a singleton reference to a Firebase root endpoint
        BotBoardApplication.myFirebaseRef = new Firebase("https://boiling-heat-9947.firebaseio.com/");

        //===GET FIREBASE BOARD PROJECT FOR THIS USER
        //get a singleton reference to a Firebase
        //myUserDataFirebaseQuery = BotBoardApplication.myFirebaseRef.child("users").orderByChild("email").equalTo(BotBoardApplication.userEmail);
        //myUserDataFirebaseQuery = BotBoardApplication.myFirebaseRef.child("users").orderByChild("email").equalTo("ubadmin3@evanschambers.com");

        // Process authentication
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.i(TAG, ">>>onCreate() : get auth token from extras");
            myAuthToken = extras.getString("AUTH_TOKEN_EXTRA");

            if(myAuthToken == null || myAuthToken.isEmpty()){
                Log.d(TAG, ">>>onCreate() : GOT A NULL authToken");
            }
        }
        else {
            Log.w(TAG, ">>>onCreate() : Users must be authenticated to do this activity. Redirecting to login activity.");
            Intent loginIntent = new Intent(getApplicationContext(), BotBoardLogin2.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
            return;
        }

        BotBoardApplication.myFirebaseRef.authWithOAuthToken("google", myAuthToken, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.v(TAG, ">>>onCreate() : Authentication worked");
                BotBoardMain2.this.myAuthData = authData;
                if (BotBoardMain2.this.myAuthData != null) {
                    //===GET FIREBASE BOARD PROJECT FOR THIS USER
                    //get a singleton reference to a Firebase
//                    myUserDataFirebaseQuery = BotBoardApplication.myFirebaseRef.child("users").orderByChild("email").equalTo(authUid);
                    myUserDataFirebaseQuery = BotBoardApplication.myFirebaseRef.child("users").orderByChild("email").equalTo("ubadmin3@evanschambers.com");
                }

                Toast.makeText(getApplicationContext(), "Authentication successful.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e(TAG, ">>>onCreate() : Authentication failed: " + firebaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Authentication failed. Please try again",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //===HOOK UP OBJECTS WITH FIREBASE TO KEEP CONCURRENT
        /*
        // Listen for floor changes
        mFirebaseRef.child("background").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String floor = dataSnapshot.getValue(String.class);
                if (floor == null || floor.equals("none")) {
                    mOfficeFloorView.setBackground(null);
                } else if (floor.equals("carpet")) {
                    mOfficeFloorView.setBackground(getResources().getDrawable(R.drawable.floor_carpet));
                } else if (floor.equals("grid")) {
                    mOfficeFloorView.setBackground(getResources().getDrawable(R.drawable.floor_grid));
                } else if (floor.equals("tile")) {
                    mOfficeFloorView.setBackground(getResources().getDrawable(R.drawable.floor_tile));
                } else if (floor.equals("wood")) {
                    mOfficeFloorView.setBackground(getResources().getDrawable(R.drawable.floor_wood));
                }
                mOfficeFloorView.invalidate();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v(TAG, "Floor update canceled: " + firebaseError.getMessage());

            }
        });

        // Listen for furniture changes
        mFirebaseRef.child("furniture").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();
                OfficeThing existingThing = dataSnapshot.getValue(OfficeThing.class);

                Log.v(TAG, "New thing added " + existingThing);

                addUpdateThingToLocalModel(key, existingThing);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                OfficeThing existingThing = dataSnapshot.getValue(OfficeThing.class);

                Log.v(TAG, "Thing changed " + existingThing);

                addUpdateThingToLocalModel(key, existingThing);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();

                Log.v(TAG, "Thing removed " + key);

                removeThingFromLocalModel(key);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                OfficeThing existingThing = dataSnapshot.getValue(OfficeThing.class);

                Log.v(TAG, "Thing moved " + existingThing);

                addUpdateThingToLocalModel(key, existingThing);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.w(TAG, "Furniture move was canceled: " + firebaseError.getMessage());
            }
        });

        // Handles menu changes that happen when an office thing is selected or de-selected
        mOfficeCanvasView.setThingFocusChangeListener(new SelectedThingChangeListener() {
            @Override
            public void thingChanged(OfficeThing officeThing) {
                mSelectedThing = officeThing;

                if (mActionMenu != null) {
                    // Clean things up, if they're there
                    mActionMenu.removeItem(R.id.action_delete);
                    mActionMenu.removeItem(R.id.action_edit);
                    mActionMenu.removeItem(R.id.action_rotate);

                    // If I have a new thing, add menu items back to it
                    if (officeThing != null) {
                        mActionMenu.add(Menu.NONE, R.id.action_delete, Menu.NONE,
                                getString(R.string.action_delete));

                        // Only desks can be edited
                        if (officeThing.getType().equals("desk")) {
                            mActionMenu.add(Menu.NONE, R.id.action_edit, Menu.NONE,
                                    getString(R.string.action_edit));
                        }

                        mActionMenu.add(Menu.NONE, R.id.action_rotate, Menu.NONE,
                                getString(R.string.action_rotate));
                    }
                }
            }
        });

        // Triggers whenever an office thing changes on the screen. This binds the
        // user interface to the scheduler that throttles updates to Firebase
        mOfficeCanvasView.setThingChangedListener(new ThingChangeListener() {
            @Override
            public void thingChanged(String key, OfficeThing officeThing) {
                mStuffToUpdate.put(key, officeThing);
                mOfficeCanvasView.invalidate();
            }
        });

        // A scheduled executor that throttles updates to Firebase to about 40ms each.
        // This prevents the high frequency change events from swamping Firebase.
        ScheduledExecutorService firebaseUpdateScheduler = Executors.newScheduledThreadPool(1);
        firebaseUpdateScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (mStuffToUpdate != null && mStuffToUpdate.size() > 0) {
                    for (OfficeThing officeThing : mStuffToUpdate.values()) {
                        updateOfficeThing(officeThing.getKey(), officeThing);
                        mStuffToUpdate.remove(officeThing.getKey());
                    }
                }
            }
        }, UPDATE_THROTTLE_DELAY, UPDATE_THROTTLE_DELAY, TimeUnit.MILLISECONDS);

         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.botboard_main_menu, menu);
        mActionMenu = menu;
        return true;
    }

    public boolean signOut(MenuItem item) {
//        Intent signOutIntent = new Intent(this, BotBoardLogin.class);
//        signOutIntent.putExtra("SIGNOUT", true);
//        startActivity(signOutIntent);
        this.finish();
        return true;
    }
}
