package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/28/16.
 */
import android.support.v4.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import xzheng2.cmu.edu.hw3.R;

import android.support.v4.app.Fragment;

public class ReportActivity extends SingleFragmentActivity {
    /** A key for passing a report ID as a long */
    public static final String EXTRA_REPORT_ID = "REPORT_ID";

    @Override
    protected Fragment createFragment() {
        long reportId = getIntent().getLongExtra(EXTRA_REPORT_ID, -1);
        if (reportId != -1) {
            return ReportFragment.newInstance(reportId);
        } else {
            return new ReportFragment();
        }
    }
}
