package xzheng2.cmu.edu.hw3.ViewActivity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.Date;

import io.fabric.sdk.android.Fabric;
import xzheng2.cmu.edu.hw3.Model.Run;
import xzheng2.cmu.edu.hw3.Model.RunManager;
import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.View.RunMapFragment;

public class StopRunActivity extends AppCompatActivity {

    private static final String TWITTER_KEY = "ZuypGQAG4z2k6CnFVXd1OZvb8";
    private static final String TWITTER_SECRET = "zFmyNkfqESQolsFW9ftshVtY5ewBkzoIvP8dMGYFMeu3yRZ2Jb";

    public static final String EXTRA_RUN_ID = "REPORT_ID";

    private long reportId;
    private TextView startTextView;
    private TextView durationTextView;
    private Button tweetButton;
    private RunManager runManager;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_run);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        Intent intent = getIntent();
        reportId = intent.getLongExtra(EXTRA_RUN_ID, -1);

        runManager = RunManager.get(this);
        startTextView = (TextView) findViewById(R.id.startedtextview);
        durationTextView = (TextView) findViewById(R.id.duration);
        tweetButton = (Button) findViewById(R.id.tweet_button);

        if (reportId != -1) {
            final Run run = runManager.getRun(reportId);
            startTextView.setText(run.getStartDate().toString());
            Date stop = new Date();
            duration = run.getDurationSeconds(stop.getTime());
            durationTextView.setText(Run.formatDuration(run.getDurationSeconds(stop.getTime())));

            tweetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TweetComposer.Builder builder = new TweetComposer.Builder(StopRunActivity.this) //should be thisactivity.this
                            .text("I am using Running Recipe, it's awsome! Start at: " + run.getStartDate() + ", Duraiton: " + Run.formatDuration(duration));
                    builder.show();
                }
            });

        }



        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_path);

        if (fragment == null) {
            fragment = RunMapFragment.newInstance(reportId);
            manager.beginTransaction()
                    .add(R.id.fragment_path, fragment)
                    .commit();
        }
    }
}
