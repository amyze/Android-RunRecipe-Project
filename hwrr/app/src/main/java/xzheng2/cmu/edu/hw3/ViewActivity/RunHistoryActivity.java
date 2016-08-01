package xzheng2.cmu.edu.hw3.ViewActivity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import xzheng2.cmu.edu.hw3.Model.Run;
import xzheng2.cmu.edu.hw3.Model.RunManager;
import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.View.RunMapFragment;


public class RunHistoryActivity extends AppCompatActivity {
    public static final String EXTRA_RUN_ID = "RUN_ID";

    TextView reportInfo;
    private static final int LOAD_LOCATIONS = 0;

    private long runId;
    private TextView startTextView;
    private TextView stopTextView;
    private TextView durationTextView;
//    private TextView distanceTextView;

    private RunManager runManager;
    private Run run;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        runManager = RunManager.get(this);

        startTextView = (TextView) findViewById(R.id.report_startedTextView);
        stopTextView = (TextView) findViewById(R.id.report_stopTextView);
        durationTextView = (TextView) findViewById(R.id.report_durationTextView);

        Intent intent = getIntent();
        runId = intent.getLongExtra(EXTRA_RUN_ID, -1);

        if (runId != -1) {
            run = runManager.getRun(runId);
            startTextView.setText(run.getStartDate().toString());
            stopTextView.setText(run.getStopDate().toString());
            durationTextView.setText(Run.formatDuration(run.getDurationSeconds(run.getStopDate().getTime())));
        }

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_fragment_holder);

        if (fragment == null) {
            fragment = RunMapFragment.newInstance(runId);
            manager.beginTransaction()
                    .add(R.id.fragment_fragment_holder, fragment)
                    .commit();
        }

    }

}
