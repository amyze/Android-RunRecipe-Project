package xzheng2.cmu.edu.hw3.ViewActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import xzheng2.cmu.edu.hw3.R;

public class LogoutappActivity extends AppCompatActivity {
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logoutapp);
        logout=(Button) findViewById(R.id.logout);
        logout.setOnClickListener(logoutButtonClicked);
    }

    View.OnClickListener logoutButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signOut();
        }
    };

    public void signOut(){
//        OAuthUser user = users.getIdentity();
//        user.logout(new OAuthUserCallback() {
//            @Override
//            public void onFinished() {
//                // user is logout and his token is expired
//            }
//            @Override
//            public void onError(String message) {
//                Log.d("error",message);
//            }
//        });
    }
}
