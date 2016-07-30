package xzheng2.cmu.edu.hw3.ViewActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import xzheng2.cmu.edu.hw3.R;


public class FitbitLogin extends AppCompatActivity {
    OAuth oauth;
    HttpURLConnection conn;
    TextView fit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit_login);
        Button fitButton = (Button) findViewById(R.id.Fibitbutton);
        fitButton.setOnClickListener(fitButtonClicked);


    }
    View.OnClickListener fitButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {



            oauth = new OAuth(FitbitLogin.this);
            oauth.initialize("4mrdLpBc5SBgHQAtLeh5zbRS0aM");

            oauth.popup("fitbit", new OAuthCallback() {
                @Override
                public void onFinished(OAuthData data) {
                    if (!data.status.equals("success"))
                        Log.d("fit",data.error);
                    else {
                         fit = (TextView)findViewById(R.id.fitTest);
//                        Log.d("fitbit test 1", "in on finished, data: "+ data.toString());
                        Log.d("fitbit token", data.token);
                        Log.d("fitbit token", data.provider);
                        Log.d("fitbit token", data.request.toString());
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                        // test1
                        HttpURLConnection con = null;
                        try {
                            URL object = new URL("https://api.fitbit.com/1/user/-/profile.json");
                            con = (HttpURLConnection) object.openConnection();
                            con.setRequestMethod("GET");
                            con.setRequestProperty("Authorization","Bearer "+data.token);
                            StringBuilder total = new StringBuilder();
                                String line = "";
                            try {


                                    InputStream inputs= con.getInputStream();
                                    BufferedReader r = new BufferedReader(new InputStreamReader(inputs));
                                    while ((line = r.readLine()) != null) {
                                        total.append(line);
                                    }
                                    Log.d("onReady() total", "onReady() total: " + total);
                                } catch (Exception e) { e.printStackTrace(); }

                                try {
                                    JSONObject result = new JSONObject(total.toString());
                                    JSONObject user = result.getJSONObject("user");
                                    fit.setText(user.getString("displayName"));

                                } catch (JSONException e){
                                    Log.e("json error", "json error: " + e.getLocalizedMessage());
                                }
                        }catch (Exception e) { e.printStackTrace(); }
                        
                    }

                }
            });

        }
    };

    public String parser(JSONObject data){
//        // parse Json
        String res ="";
        try {
            JSONObject  jsonRootObject = data;

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("activities");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int steps = Integer.parseInt(jsonObject.optString("steps").toString());
                int calories = Integer.parseInt(jsonObject.optString("calories").toString());
//                                String calories = jsonObject.optString("calories").toString();


                float distance = Float.parseFloat(jsonObject.optString("distance").toString());

                res = "steps= "+ steps +" \n calories= "+ calories +" \n distance= "+ distance +" \n ";
            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }


}
