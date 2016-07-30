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
                            URL object = new URL("https://api.fitbit.com/1/user/-/activities/date/today.json");
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



                                    fit.setText(parser(total.toString()));


                        }catch (Exception e) { e.printStackTrace(); }

                    }

                }
            });

        }
    };

    public String parser(String input){
//        // parse Json
        String res = "";
        try {
            JSONObject  jsonRootObject = new JSONObject(input);
            // get calories
            JSONObject summary = jsonRootObject.getJSONObject("summary");
            int calories = Integer.parseInt(summary.optString("caloriesOut").toString());
            // get steps
            int steps = Integer.parseInt(summary.getString("steps"));
            // get distance

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = summary.optJSONArray("distances");


            //Iterate the jsonArray and print the info of JSONObjects

                JSONObject total = jsonArray.getJSONObject(0);

                float distance = Float.parseFloat(total.optString("distance").toString());





                res = "steps= "+ steps +" \n calories= "+ calories +" \n distance= "+ distance +" \n ";

            return res;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }


}
