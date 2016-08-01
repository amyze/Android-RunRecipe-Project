package xzheng2.cmu.edu.hw3.ViewActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.Twitter.ConstantValues;


public class Login extends AppCompatActivity {
    OAuth oauth;
    HttpURLConnection conn;
    TextView fit;
    RequestToken requestToken ;
    AccessToken accessToken;
    String oauth_url,oauth_verifier,profile_url;
    Dialog auth_dialog;
    WebView web;
    SharedPreferences pref;
    ProgressDialog progress;
    Twitter twitter;
    private  int statusCode;
    private static final String TAG = "TweetService";
    private static final String TAG1 = "info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);
        ImageButton gooButton = (ImageButton) findViewById(R.id.goobutton);
        gooButton.setOnClickListener(gooButtonClicked);
        ImageButton twiButton =(ImageButton)findViewById(R.id.Tbutton);
        twiButton.setOnClickListener(twiButtonClicked);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(logoutButtonClicked);

        pref = PreferenceManager.getDefaultSharedPreferences(Login.this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("CONSUMER_KEY", ConstantValues.TWITTER_CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", ConstantValues.TWITTER_CONSUMER_SECRET);
        edit.commit();

        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(pref.getString("CONSUMER_KEY", ""), pref.getString("CONSUMER_SECRET", ""));


    }
    View.OnClickListener gooButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            oauth = new OAuth(Login.this);
            oauth.initialize("4mrdLpBc5SBgHQAtLeh5zbRS0aM");
            oauth.popup("google_plus", new OAuthCallback() {
                @Override
                public void onFinished(OAuthData data) {
                    if (!data.status.equals("success"))
                        Log.d("fit",data.error);
                    else {
                        Toast.makeText(Login.this, "login success", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    };

    View.OnClickListener twiButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            loginTwitter();
        }
    };
    View.OnClickListener logoutButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
        }
    };
    public void loginTwitter(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
        Log.d(TAG, sharedPreferences.toString());
        //PREFERENCE_TWITTER_IS_LOGGED_IN will only be set after user successfully authenticates himself
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            Log.d(TAG, ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN);
            new TokenGet().execute();
        }
    }
    //GET Token
    private class TokenGet extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {

            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
                Log.d(TAG1, oauth_url);
                URL url = new URL(oauth_url);
                conn = (HttpURLConnection) url.openConnection();
                statusCode = conn.getResponseCode();
                String code = ""+statusCode;
                Log.d("httpcode", code);
            } catch (TwitterException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return oauth_url;
        }
        @Override
        protected void onPostExecute(String oauth_url) {
            if(oauth_url != null){
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(Login.this);
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                auth_dialog.setContentView(R.layout.oauth_dialog);
                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && authComplete == false){
                            authComplete = true;
                            Log.d("URL",url);
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");
                            auth_dialog.dismiss();
                            new AccessTokenGet().execute();
                        }else if(url.contains("denied")){
                            auth_dialog.dismiss();
                            Toast.makeText(Login.this, "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Log.d("test 1", auth_dialog.toString());
                auth_dialog.show();
                auth_dialog.setCancelable(true);
            }else{
                Toast.makeText(Login.this, "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AccessTokenGet extends AsyncTask<String, String, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Login.this);
            progress.setMessage("Fetching Data ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }


        @Override
        protected Boolean doInBackground(String... args) {

            try {
                accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("ACCESS_TOKEN", accessToken.getToken());
                edit.putString("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());
                edit.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
                User user = twitter.showUser(accessToken.getUserId());
                profile_url = user.getOriginalProfileImageURL();
                edit.putString("NAME", user.getName());
                edit.putString("IMAGE_URL", user.getOriginalProfileImageURL());
                edit.commit();


            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        }
        @Override
        protected void onPostExecute(Boolean response) {
            if(response){
                progress.hide();
            }
        }


    }
}
