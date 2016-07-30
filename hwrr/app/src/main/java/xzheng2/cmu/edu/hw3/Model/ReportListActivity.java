package xzheng2.cmu.edu.hw3.Model;

import android.support.v4.app.Fragment;

public class ReportListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ReportListFragment();
    }

}
