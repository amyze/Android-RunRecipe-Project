package xzheng2.cmu.edu.hw3.ViewActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.Twitter.ConstantValues;

public class ViewEventActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String message;

    RequestToken requestToken ;
    AccessToken accessToken;
    String oauth_url,oauth_verifier,profile_url;
    Dialog auth_dialog;
    WebView web;
    SharedPreferences pref;
    ProgressDialog progress;
    Twitter twitter;
    private HttpURLConnection conn;
    private  int statusCode;
    private static final String TAG1 = "info";
    private static final String TAG = "TweetService";


    private GoogleMap googleMap;
    private Place latestPlace;
    private String locationOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TWEET
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("CONSUMER_KEY", ConstantValues.TWITTER_CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", ConstantValues.TWITTER_CONSUMER_SECRET);
        edit.commit();

        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(pref.getString("CONSUMER_KEY", ""), pref.getString("CONSUMER_SECRET", ""));
        //SHOW Text
        setContentView(R.layout.activity_view_event2);
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.Eventmap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("title");
        String start = intent.getStringExtra("start");
        String start2 = start.substring(5,10)+" "+ start.substring(11,16)+"-"+start.substring(25,29);
        String end = intent.getStringExtra("end");
        String end2 = end.substring(5,10)+" "+ end.substring(11,16)+" "+end.substring(25,29);
        String location = intent.getStringExtra("location");
        locationOnMap = location;

        TextView title = (TextView)findViewById(R.id.titleTextView);
        title.setText(name);

        TextView startTime  = (TextView)findViewById(R.id.startTextView);
        startTime.setText(start2);

        TextView endTime =(TextView)findViewById(R.id.endTextView);
        endTime.setText(end2);

        TextView eventLocation =(TextView)findViewById(R.id.LocationTextView);
        eventLocation.setText(location);



        Button tweetButton = (Button) findViewById(R.id.TweetEvent);
        tweetButton.setOnClickListener(tweetButtonClicked);
        message = "@MobileApp4"+" "+ name + "\n"+ start2 +"\n"+ end2 +"\n"+ location;
        if(message.length()>=140){
            message= message.substring(0,139);
        }
    }

    private void doShowItOnMap() {
        googleMap.clear();
        //
        List<Address> addressList = null;
        if (locationOnMap == null || locationOnMap.equals("")){
            return;
        }
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(locationOnMap, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList.size() == 0) {
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show();
            return;
        }
        android.location.Address address = addressList.get(0);

        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        //
//        LatLng latLng = latestPlace.getLatLng();
//        String address = latestPlace.getAddress().toString();
        googleMap.addMarker(new MarkerOptions().position(latLng).title(locationOnMap));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), null);
//        latestPlace = null;
    }

    public void onError(Status status) {
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onMapReady(GoogleMap map) {
//        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        this.googleMap = map;
        if (map != null) {
            doShowItOnMap();
        } else {
            Toast.makeText(this, "No result"+"\n"+"Please double check the address", Toast.LENGTH_SHORT).show();
//            googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        }
    }

    View.OnClickListener tweetButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {


            logIn();

        }
    };

    private void logIn() {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d(TAG, sharedPreferences.toString());
        //PREFERENCE_TWITTER_IS_LOGGED_IN will only be set after user successfully authenticates himself
        if (!sharedPreferences.getBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false))
        {
            Log.d(TAG, ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN);

            new TokenGet().execute();
        }
        else
        {

            new PostTweet().execute();
        }
    }

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
                // TODO Auto-generated catch block
                String code = ""+statusCode;
                Log.d("httpcode", code);
                e.printStackTrace();
            } catch (MalformedURLException e) {
                String code = ""+statusCode;
                Log.d("httpcode", code);
                e.printStackTrace();
            } catch (IOException e) {
                String code = ""+statusCode;
                Log.d("httpcode", code);
                e.printStackTrace();
            }
            return oauth_url;
        }
        @Override
        protected void onPostExecute(String oauth_url) {
            if(oauth_url != null){
                Log.e("URL", oauth_url);
                auth_dialog = new Dialog(ViewEventActivity.this);
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
                            Toast.makeText(getApplicationContext(), "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Log.d("test 1", auth_dialog.toString());
                auth_dialog.show();
                auth_dialog.setCancelable(true);
            }else{
                Toast.makeText(getApplicationContext(), "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AccessTokenGet extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ViewEventActivity.this);
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
                new PostTweet().execute();
            }
        }
    }

    private class PostTweet extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ViewEventActivity.this);
            progress.setMessage("Posting tweet ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }
        protected String doInBackground(String... args) {

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(pref.getString("CONSUMER_KEY", ""));
            builder.setOAuthConsumerSecret(pref.getString("CONSUMER_SECRET", ""));
            AccessToken accessToken = new AccessToken(pref.getString("ACCESS_TOKEN", ""), pref.getString("ACCESS_TOKEN_SECRET", ""));
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
            String status = "" +  message  ;
            try {

                twitter4j.StatusUpdate statusUpdate = new StatusUpdate("Hello Twitter");
                twitter4j.Status response = twitter.updateStatus(status);
                return response.toString();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String res) {
            if(res != null){
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Tweet Sucessfully Posted", Toast.LENGTH_SHORT).show();
            }else{
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Error while tweeting !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
