package xzheng2.cmu.edu.hw3.View;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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

//import android.app.Fragment;

/**
 * Created by zhengqian1 on 6/5/16.
 */

public class IDriftFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG1 = "info";
    private static final String TAG = "TweetService";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HttpURLConnection conn;
    private  int statusCode;
    private static String message;
    RequestToken requestToken ;
    AccessToken accessToken;
    String oauth_url,oauth_verifier,profile_url;
    Dialog auth_dialog;
    WebView web;
    SharedPreferences pref;
    ProgressDialog progress;
    Twitter twitter;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     // * @param param1 Parameter 1.
     //* @param param2 Parameter 2.
     * @return A new instance of fragment IDriftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IDriftFragment newInstance(String param1, String param2) {
        IDriftFragment fragment = new IDriftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IDriftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("CONSUMER_KEY", ConstantValues.TWITTER_CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", ConstantValues.TWITTER_CONSUMER_SECRET);
        edit.commit();

        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(pref.getString("CONSUMER_KEY", ""), pref.getString("CONSUMER_SECRET", ""));


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        View rootView = inflater.inflate(R.layout.fragment_idrift, container, false);
        //set the tweet button
        Button tweetButton = (Button) rootView.findViewById(R.id.tweetbutton);
        tweetButton.setOnClickListener(tweetButtonClicked);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = sdf.format(new Date())+" EDT";
        TextView resultView = (TextView)rootView.findViewById(R.id.datetime);
          resultView.setText(currentDateTime);
    // set version
        TextView versionView = (TextView)rootView.findViewById(R.id.version);
        String phoneModel = Build.MODEL;
        String androidVersion = Build.VERSION.RELEASE;
        versionView.setText(""+phoneModel+" "+androidVersion);
        String andrewID = ((TextView) rootView.findViewById(R.id.andrewID)).getText().toString();
        String version = ((TextView) rootView.findViewById(R.id.version)).getText().toString();
        // print out in console
        Log.d(TAG1, andrewID);
        Log.d(TAG1, version);
        Log.d(TAG1, currentDateTime);
        message= "@MobileApp4"+"[ "+ andrewID+" ]"+version+" "+currentDateTime;
        return rootView;
    }


    View.OnClickListener tweetButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
//            Toast.makeText(getActivity(), "button is good", Toast.LENGTH_SHORT).show();
            logIn();
        }
    };

    private void logIn() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
                auth_dialog = new Dialog(getActivity());
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
                            Toast.makeText(getActivity(), "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
                Log.d("test 1", auth_dialog.toString());
                auth_dialog.show();
                auth_dialog.setCancelable(true);



            }else{

                Toast.makeText(getActivity(), "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();


            }
        }
    }

    private class AccessTokenGet extends AsyncTask<String, String, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
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
                //
            }
        }


    }

    private class PostTweet extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
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




                //File file = new File("//imageFilePath");
                //statusUpdate.setMedia(file);
                //twitter4j.Status response = twitter.updateStatus(statusUpdate);

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
                Toast.makeText(getActivity(), "Tweet Sucessfully Posted", Toast.LENGTH_SHORT).show();

            }else{
                progress.dismiss();
                Toast.makeText(getActivity(), "Error while tweeting !", Toast.LENGTH_SHORT).show();

            }

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(String id) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(id);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and
        public void onFragmentInteraction(String id);
    }

}
