package xzheng2.cmu.edu.hw3.View;

import android.support.v4.app.Fragment;

public class RunPathActivity extends SingleFragment {
    /** A key for passing a report ID as a long */
    public static final String EXTRA_RUN_ID = "RUN_ID";

    @Override
    protected Fragment createFragment() {
        long reportId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        System.out.println("*****id:" + reportId);
        if (reportId != -1) {
            return RunMapFragment.newInstance(reportId);
        } else {
            return new RunMapFragment();
        }
    }
}
