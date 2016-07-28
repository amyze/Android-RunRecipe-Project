package xzheng2.cmu.edu.hw3.ViewActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import xzheng2.cmu.edu.hw3.R;


public class FitbitLogin extends AppCompatActivity {
    OAuth oauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit_login);
        Button fitButton = (Button) findViewById(R.id.Fibitbutton);
        fitButton.setOnClickListener(fitButtonClicked);
        oauth = new OAuth(this);

    }
    View.OnClickListener fitButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {



            oauth.initialize("4mrdLpBc5SBgHQAtLeh5zbRS0aM");

            oauth.popup("fitbit", new OAuthCallback() {
                @Override
                public void onFinished(OAuthData data) {
                    if (data.status.equals("error"))
                        Log.d("fit",data.error);
                    else {
                        TextView fit = (TextView)findViewById(R.id.fitTest);
//                        fit.setText("123");

                        // test Json parser part
                        String strJson="{\n" +
                                "    \"activities\":[\n" +
                                "        {\n" +
                                "            \"activityId\":51007,\n" +
                                "            \"activityParentId\":90019,\n" +
                                "            \"calories\":230,\n" +
                                "            \"description\":\"7mph\",\n" +
                                "            \"distance\":2.04,\n" +
                                "            \"duration\":1097053,\n" +
                                "            \"hasStartTime\":true,\n" +
                                "            \"isFavorite\":true,\n" +
                                "            \"logId\":1154701,\n" +
                                "            \"name\":\"Treadmill, 0% Incline\",\n" +
                                "            \"startTime\":\"00:25\",\n" +
                                "            \"steps\":3783\n" +
                                "        }\n" +
                                "    ],\n" +
                                "    \"goals\":{\n" +
                                "        \"caloriesOut\":2826,\n" +
                                "        \"distance\":8.05,\n" +
                                "        \"floors\":150,\n" +
                                "        \"steps\":10000\n" +
                                "     },\n" +
                                "    \"summary\":{\n" +
                                "        \"activityCalories\":230,\n" +
                                "        \"caloriesBMR\":1913,\n" +
                                "        \"caloriesOut\":2143,\n" +
                                "        \"distances\":[\n" +
                                "            {\"activity\":\"tracker\", \"distance\":1.32},\n" +
                                "            {\"activity\":\"loggedActivities\", \"distance\":0},\n" +
                                "            {\"activity\":\"total\",\"distance\":1.32},\n" +
                                "            {\"activity\":\"veryActive\", \"distance\":0.51},\n" +
                                "            {\"activity\":\"moderatelyActive\", \"distance\":0.51},\n" +
                                "            {\"activity\":\"lightlyActive\", \"distance\":0.51},\n" +
                                "            {\"activity\":\"sedentaryActive\", \"distance\":0.51},\n" +
                                "            {\"activity\":\"Treadmill, 0% Incline\", \"distance\":3.28}\n" +
                                "        ],\n" +
                                "        \"elevation\":48.77,\n" +
                                "        \"fairlyActiveMinutes\":0,\n" +
                                "        \"floors\":16,\n" +
                                "        \"lightlyActiveMinutes\":0,\n" +
                                "        \"marginalCalories\":200,\n" +
                                "        \"sedentaryMinutes\":1166,\n" +
                                "        \"steps\":0,\n" +
                                "        \"veryActiveMinutes\":0\n" +
                                "    }\n" +
                                "}";
                        // parse Json
                        String res ="";
                        try {
                            JSONObject  jsonRootObject = new JSONObject(strJson);

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
                            fit.setText(res);
                        } catch (JSONException e) {e.printStackTrace();}

                    }
                }
            });







        }
    };


}
