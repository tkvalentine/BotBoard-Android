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
import android.view.View;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;

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
    public static final String AUTH_TOKEN_EXTRA = "authToken";

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, ">>>onCreate() : ENTERED");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

        /* Load the Google login button */
// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();
        SignInButton googleLoginButton = (SignInButton) findViewById(R.id.login_with_google2);
//        googleLoginButton.setScopes(gso.getScopeArray());

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
         //       .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
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
            signOut();
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
                     * Google Play services is installed. */
                    Log.e(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token : Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }

                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                Log.e(TAG, ">>>getGoogleOAuthTokenAndLogin() : async task get Google AUTH token call : CALLING BotBoardMain ACTIVITY");

                mGoogleLoginClicked = false;
                Intent intentWithToken = new Intent(BotBoardLogin.this, BotBoardMain.class);
                intentWithToken.putExtra(AUTH_TOKEN_EXTRA, token);
                startActivity(intentWithToken);
            }
        };

        Log.i(TAG, ">>>getGoogleOAuthTokenAndLogin() : start async task to get Google AUTH token");
        task.execute();
    }

    public void signOut() {
        Log.i(TAG, ">>>signOut() : ENTER");
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }
}