package com.evanschambers.botboard;

/**
 * Created by timvalentine on 3/3/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.evanschambers.botboard.datamodels.BotBoardFirebaseRecord;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.json.JSONObject;

import java.io.IOException;

//import com.google.android.gms.auth.api.Auth;

/**
 * This class implements Google+ Sign-in. There's not much Firebase specific stuff here.
 *
 * If you'd like to learn more about Google+ Sign-in, you can find the official documentation here:
 * https://developers.google.com/+/mobile/android/sign-in
 */
public class BotBoardLogin extends Activity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = BotBoardLogin.class.getSimpleName();

    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;
    public static final int RC_DECK_BROWSER_ACTIVITY = 2;
    public static final String AUTH_TOKEN_EXTRA = "authToken";

    private Menu mActionMenu;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;

    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
     * without waiting. */
    private boolean mGoogleLoginClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can resolve them when the user clicks
     * sign-in. */
    private ConnectionResult mGoogleConnectionResult;

    //get a singleton reference to a Firebase root user endpoint
    private Query myUserDataFirebaseQuery;
    private String myAuthToken = null;
    private AuthData myAuthData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, ">>>onCreate() : ENTERED");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.botboard_login_layout);

        /* Load the Google login button */
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        SignInButton googleLoginButton = (SignInButton) findViewById(R.id.login_with_google2);
        //googleLoginButton.setScopes(gso.getScopeArray());

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleLoginClicked = true;

                if (!mGoogleApiClient.isConnecting()) {
                    if (mGoogleConnectionResult != null) {
                        Log.i(TAG, ">>>onCreate() : LOGIN BUTTON CLICKED : signin error : goto resolve");
                        resolveSignInError();
                    } else if (mGoogleApiClient.isConnected()) {
                        Log.i(TAG, ">>>onCreate() : LOGIN BUTTON CLICKED : signin good : get auth token and login");
                        getGoogleOAuthTokenAndLogin();
                    } else {
                    /* connect API now */
                        Log.d(TAG, ">>>onCreate() : LOGIN BUTTON CLICKED : Trying to connect to Google API");
                        mGoogleApiClient.connect();
                }
                }
            }
        });

        /* Setup the Google API object to allow Google+ logins */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                        //.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, ">>>onStart() : ENTERED");
        super.onStart();
        Intent intent = getIntent();
        boolean signout = intent.getBooleanExtra("SIGNOUT", false);

        if(signout) {
            Log.d(TAG, ">>>onStart() : signing out");
            quitApp();
        } else if (!mGoogleIntentInProgress) {
            Log.d(TAG, ">>>onStart() : connect to Google API client");
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, ">>>onConnected() : ENTERED");
        /* Connected with Google API, use this to authenticate with Firebase */
        Log.i(TAG, ">>>onConnected() : Login Connected : get auth token and login");
        getGoogleOAuthTokenAndLogin();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, ">>>onConnectionSuspended() : ENTERED : Login Suspended");
        // ignore
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, ">>>onConnectionFailed() : ENTER");

        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = connectionResult;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                Log.e(TAG, ">>>onConnectionFailed() : login button clicked so pass thru to resolve");
            } else {
                Log.e(TAG, ">>>onConnectionFailed() : " + connectionResult.toString());
            }
        }
    }

    /**
     * This method fires when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, ">>>onActivityResult() : ENTER");

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_LOGIN) {
            Log.i(TAG, ">>>onActivityResult() : FROM GOOGLE LOGIN");

            /* This was a request by the Google API */
            if (resultCode != RESULT_OK) {
                Log.i(TAG, ">>>onActivityResult() : result not OK");
                mGoogleLoginClicked = false;
            }

            mGoogleIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                Log.i(TAG, ">>>onActivityResult() : retry Google API client reconnect");
                mGoogleApiClient.connect();
            }
        } else if (requestCode == RC_DECK_BROWSER_ACTIVITY) {
            if (data != null) {
                //may have finished before returning result
                boolean signout = data.getBooleanExtra("SIGNOUT", false);

                if (signout) {
                    Log.d(TAG, ">>>onActivityResult() : signing out");
                    quitApp();
                } else if (!mGoogleIntentInProgress) {
                    Log.d(TAG, ">>>onActivityResult() : connect to Google API client");
                    // auto sign in
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        Log.i(TAG, ">>>resolveSignInError() : ENTER");

        if (mGoogleConnectionResult.hasResolution()) {
            Log.i(TAG, ">>>resolveSignInError() : has resolution");

            try {
                Log.i(TAG, ">>>resolveSignInError() : restart GOOGLE LOGIN");
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, ">>>resolveSignInError() : reconnect Google API Client");
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getGoogleOAuthTokenAndLogin() {
        Log.i(TAG, ">>>getGoogleOAuthTokenAndLogin() : ENTER");

        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;
            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    Log.i(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token call made");
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(BotBoardLogin.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token call : Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, ">>>getGoogleOAuthTokenAndLogin() : Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {
                        Log.i(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token restart GOOGLE LOGIN");
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed.
                     */
                    Log.e(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token : Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }

                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                Log.e(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token call : CALLING BotBoardMain ACTIVITY");

                //initiate Firebase connection and DB access, retrieve or build default user record
                mGoogleLoginClicked = false;
                if (token != null && !token.isEmpty()) {
                    getFirebaseConnectionAndUserData(token);
                } else {
                    Log.e(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token call : TOKEN NOT RETRIEVED");
                }
            }
        };

        Log.i(TAG, ">>>getGoogleOAuthTokenAndLogin() : start async task to get Google AUTH token");
        task.execute();
    }

    private void getFirebaseConnectionAndUserData(String token) {
        //initialize Firebase
        Firebase.setAndroidContext(this);

        //===GET FIREBASE REFERENCE
        //get a singleton reference to a Firebase root endpoint
        BotBoardApplication.myFirebaseRef = new com.firebase.client.Firebase("https://boiling-heat-9947.firebaseio.com/");

        // Process authentication
        Bundle extras = getIntent().getExtras();
        if (token != null && !token.isEmpty()) {
            Log.i(TAG, ">>>onCreate() : get auth token from extras");
            myAuthToken = token;

            if (myAuthToken == null || myAuthToken.isEmpty()) {
                Log.d(TAG, ">>>getFirebaseConnectionAndUserData() : GOT A NULL authToken");
            } else {
                Log.d(TAG, ">>>getFirebaseConnectionAndUserData() : GOT A valid authToken");
            }
        } else {
            Log.w(TAG, ">>>getFirebaseConnectionAndUserData() : Users must be authenticated to do this activity. Redirecting to login activity.");

            Intent loginIntent = new Intent(getApplicationContext(), BotBoardLogin.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
            return;
        }

        Log.v(TAG, ">>>getFirebaseConnectionAndUserData() : getting auth data");

        BotBoardApplication.myFirebaseRef.authWithOAuthToken("google", myAuthToken, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.v(TAG, ">>>getFirebaseConnectionAndUserData() : AuthResultHandler() : ENTER");
                myAuthData = authData;
                if (myAuthData != null) {
                    Log.v(TAG, ">>>getFirebaseConnectionAndUserData() : AuthResultHandler() : got authData");
                    java.util.Map<String, Object> auth = myAuthData.getAuth();
                    Log.v(TAG, ">>>getFirebaseConnectionAndUserData() : AuthResultHandler() : myAuthData.getAuth() = " + auth.toString());
                    //String googleAuthIdRaw = (String)auth.get("uid");
                    //String[] splits = googleAuthIdRaw.split(":");
                    //String uid = null;
                    //if(splits.length == 2){
                    //   uid = splits[1];
                    //}

                    BotBoardApplication.userToken = myAuthData.getToken();
                    BotBoardApplication.userName = myAuthData.getProviderData().get("displayName").toString();
                    BotBoardApplication.userEmail = myAuthData.getProviderData().get("email").toString();
                    BotBoardApplication.userId = myAuthData.getProviderData().get("id").toString();
                    BotBoardApplication.userAuthProvider = myAuthData.getProvider().toString();
                    BotBoardApplication.userPhotoURL = android.net.Uri.parse(myAuthData.getProviderData().get("profileImageURL").toString());

                    Log.v(TAG, ">>>getFirebaseConnectionAndUserData() : AuthResultHandler() :" +
                            " userToken=" + BotBoardApplication.userToken +
                            " userName=" + BotBoardApplication.userName +
                            " userEmail=" + BotBoardApplication.userEmail +
                            " userId=" + BotBoardApplication.userId +
                            " userPhotoURL=" + BotBoardApplication.userPhotoURL);

                    //===GET FIREBASE BOARD PROJECT FOR THIS USER
                    //get a singleton reference to a Firebase
                    myUserDataFirebaseQuery = BotBoardApplication.myFirebaseRef.child("users/" + BotBoardApplication.userId);

                    // Attach an listener to read the data at our posts reference
                    myUserDataFirebaseQuery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot snapshot) {
                            Log.v(TAG, ">>>getFirebaseConnectionAndUserData().onAuthenticated() : myUserDataFirebaseQuery.onDataChange() : snapshot =" + snapshot.toString());
                            JSONObject snapShotJSONObject = null;
                            //DataSnapshot { key = 113250030596853243779, value = null }
                            Object snapshotValueObject = snapshot.getValue();

                            if (snapshotValueObject != null) {
                                //if this is app startup and the response from the query for myUserDataFirebaseQuery
                                //then we need to load the user's record from Firebase
                                if (BotBoardApplication.isInitialFirebaseAccess) {
                                    BotBoardApplication.userRecord = snapshot.getValue(BotBoardFirebaseRecord.class);

                                    BotBoardApplication.isInitialFirebaseAccess = false;
                                }
                            } else {
                                //no data for this Google uathenticated user
                                //create a user node in Firebase
                                BotBoardApplication.userRecord =
                                        BotBoardFirebaseRecord.createDefaultUserRecord(BotBoardApplication.userId,
                                                BotBoardApplication.userName,
                                                BotBoardApplication.userEmail,
                                                BotBoardApplication.userAuthProvider);

                                //String newUserJSONValuesString = BotBoardApplication.userRecord.toJSONValueString();
                                BotBoardApplication.myFirebaseRef.child("users")
                                        .child(BotBoardApplication.userId)
//                                            .setValue(newUserJSONValuesString, new Firebase.CompletionListener() {
                                        .setValue(BotBoardApplication.userRecord, new Firebase.CompletionListener() {
                                            @Override
                                            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                                if (firebaseError != null) {
                                                    Log.w(TAG, "Update failed! " + firebaseError.getMessage());
                                                }
                                            }
                                        });

                                BotBoardApplication.isInitialFirebaseAccess = false;
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.v(TAG, ">>>getFirebaseConnectionAndUserData().onAuthenticated() : myUserDataFirebaseQuery.onCancelled() : error =" + firebaseError.getMessage());
                        }
                    });

/*
                    // create the firebase for the RESTFUL access
                    try {
                        Log.v(TAG, ">>>onCreate() : AuthResultHandler() : get a firebase restful object");
                        //net.thegreshams.firebase4j.service.Firebase firebaseRestful = new net.thegreshams.firebase4j.service.Firebase("https://boiling-heat-9947.firebaseio.com", myAuthToken);
                        //net.thegreshams.firebase4j.service.Firebase firebaseRestful = new net.thegreshams.firebase4j.service.Firebase("https://boiling-heat-9947.firebaseio.com", uid);
                        //net.thegreshams.firebase4j.service.Firebase firebaseRestful = new net.thegreshams.firebase4j.service.Firebase("https://boiling-heat-9947.firebaseio.com", BotBoardApplication.userToken);
                        net.thegreshams.firebase4j.service.Firebase firebaseRestful = new net.thegreshams.firebase4j.service.Firebase("https://boiling-heat-9947.firebaseio.com", BotBoardApplication.appSecret);

//                        String encodedUrl = URLEncoder.encode("https://boiling-heat-9947.firebaseio.com", "UTF-8");
//                        String encodedSecret = URLEncoder.encode(BotBoardApplication.appSecret, "UTF-8");
//                        net.thegreshams.firebase4j.service.Firebase firebaseRestful = new net.thegreshams.firebase4j.service.Firebase(encodedUrl, encodedSecret);
                        Log.v(TAG, ">>>onCreate() : AuthResultHandler() : call GET on firebase restful object");
                        // "GET" () - the root data object - i.e. all users and data
                        //FirebaseResponse response = firebaseRestful.get();
                        FirebaseResponse response = firebaseRestful.get("/users");
//                        String encodedPath = URLEncoder.encode("/users", "UTF-8");
//                        FirebaseResponse response = firebaseRestful.get(encodedPath);
                        Log.v(TAG, ">>>onCreate() : AuthResultHandler() : response="+response);

                        String guts = response.getRawBody();
                        String gutsFormatted = null;
                        try {
                            gutsFormatted = (new JSONObject(guts)).toString(4);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.v(TAG, ">>>onCreate() : AuthResultHandler() : restful result = " + gutsFormatted);
                    }catch(FirebaseException e){
                        System.out.println("\n\nResult of GET (FirebaseException):\n" + e.getMessage());
                    }catch(java.io.UnsupportedEncodingException e){
                        System.out.println("\n\nResult of GET (UnsupportedEncodingException):\n" + e.getMessage());
                    }
*/
                }

                Toast.makeText(getApplicationContext(), "Authentication successful.",
                        Toast.LENGTH_SHORT).show();

                startMainAppActivity();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e(TAG, ">>>getFirebaseConnectionAndUserData() : Authentication failed: " + firebaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Authentication failed. Please try again",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMainAppActivity() {
        mGoogleLoginClicked = false;
        Intent intent = new Intent(BotBoardLogin.this, BotBoardMain.class);
        startActivityForResult(intent, RC_DECK_BROWSER_ACTIVITY);
    }

    public void quitApp(MenuItem menuItem) {
        Log.i(TAG, ">>>quitApp(menuItem) : ENTER");
        quitApp();
    }

    public void quitApp() {
        Log.i(TAG, ">>>quitApp() : ENTER");

        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.botboard_login_menu, menu);
        mActionMenu = menu;
        return true;
    }
}