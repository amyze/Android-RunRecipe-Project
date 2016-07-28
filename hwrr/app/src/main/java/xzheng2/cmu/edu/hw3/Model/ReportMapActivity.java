package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/28/16.
 */
import android.support.v4.app.Fragment;

public class ReportMapActivity extends SingleFragmentActivity {
    /** A key for passing a report ID as a long */
    public static final String EXTRA_REPORT_ID = "REPORT_ID";

    @Override
    protected Fragment createFragment() {
        long reportId = getIntent().getLongExtra(EXTRA_REPORT_ID, -1);
        if (reportId != -1) {
            return ReportMapFragment.newInstance(reportId);
        } else {
            return new ReportMapFragment();
        }
    }

}
