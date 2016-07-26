package xzheng2.cmu.edu.hw3.ViewActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by chengcheng on 7/26/16.
 */
public class RunActivity extends FragmentActivity {
    public static final String EXTRA_RUN_ID = "RUN_ID";

    protected Fragment createFragment() {
        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        if (runId != -1) {
            return RunFragment.newInstance(runId);
        } else {
            return new RunFrangment();
        }

    }
}
