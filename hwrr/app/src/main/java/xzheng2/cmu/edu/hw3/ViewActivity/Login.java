package xzheng2.cmu.edu.hw3.ViewActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import io.oauth.OAuth;
import io.oauth.OAuthCallback;
import io.oauth.OAuthData;
import xzheng2.cmu.edu.hw3.R;


public class Login extends AppCompatActivity {
    OAuth oauth;
    HttpURLConnection conn;
    TextView fit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_login);
        ImageButton fitButton = (ImageButton) findViewById(R.id.goobutton);
        fitButton.setOnClickListener(gooButtonClicked);


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
//                        Log.d("fitbit test 1", "in on finished, data: "+ data.toString());
//                        Log.d("fitbit token", data.token);
//                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
//                        HttpURLConnection con = null;
//                        try {
//                            URL object = new URL("https://api.fitbit.com/1/user/-/activities/date/today.json");
//                            con = (HttpURLConnection) object.openConnection();
//                            con.setRequestMethod("GET");
//                            con.setRequestProperty("Authorization","Bearer "+data.token);
//                            StringBuilder total = new StringBuilder();
//                            String line = "";
//                            try {
//                                InputStream inputs= con.getInputStream();
//                                BufferedReader r = new BufferedReader(new InputStreamReader(inputs));
//                                while ((line = r.readLine()) != null) {
//                                    total.append(line);
//                                }
//                                Log.d("onReady() total", "onReady() total: " + total);
//                            } catch (Exception e) { e.printStackTrace(); }
//                            parser(total.toString());
//                        }catch (Exception e) { e.printStackTrace(); }

                    }

                }
            });
        }
    };

}
