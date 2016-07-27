package xzheng2.cmu.edu.hw3.View;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xzheng2.cmu.edu.hw3.R;

public class RunPathActivity extends AppCompatActivity {
    public static final String EXTRA_RUN_ID = "RUN_ID";

    protected Fragment createFragment() {

        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        if(runId != -1) {
            return RunMapFragment.newInstance(runId);
        } else {
            return new RunMapFragment();
        }
    }



}
