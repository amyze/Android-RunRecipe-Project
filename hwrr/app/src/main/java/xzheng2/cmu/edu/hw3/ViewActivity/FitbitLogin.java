package xzheng2.cmu.edu.hw3.ViewActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
// Initialize the SDK

    }
    View.OnClickListener fitButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {


//            oauth.initialize("hKTtZPm_xLB5_IYrqvmJxPSaSdQ");
            oauth.initialize("4mrdLpBc5SBgHQAtLeh5zbRS0aM");

            oauth.popup("fitbit", new OAuthCallback() {
                @Override
                public void onFinished(OAuthData data) {
                    if (data.status.equals("error"))
                        Log.d("fit",data.error);
                    else {
                        TextView fit = (TextView)findViewById(R.id.fitTest);
                        fit.setText("123");
                    }
                }
            });

        }
    };


}
