package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/28/16.
 */
import android.support.v4.app.Fragment;

public class ReportListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ReportListFragment();
    }

}