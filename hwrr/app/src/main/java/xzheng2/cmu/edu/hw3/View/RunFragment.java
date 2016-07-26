package xzheng2.cmu.edu.hw3.View;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.Run.LocationReceiver;
import xzheng2.cmu.edu.hw3.Run.RunManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment {
    private static final String TAG = "RunFragment";
    private static final String RUN_ID = "RUN_ID";
    private static final int LOAD_RUN = 0;
    private static final int LOAD_LOCATION = 1;
    private Run mRun;
    private Location mLastLocation;
    private RunManager mRunManager;


    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

        protected void onLocationReceiverd(Context context, Location loc) {
            if (!mRunManager.isTrackingReport(mReport))
                return;
            mLastLocation = loc;
            if (isVisible())
                updateUI();
        }
    };

    public RunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false);
    }


}
