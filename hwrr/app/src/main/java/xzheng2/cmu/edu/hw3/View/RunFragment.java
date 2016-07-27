package xzheng2.cmu.edu.hw3.View;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.interfaces.DSAPublicKey;

import xzheng2.cmu.edu.hw3.Model.LastLocationLoader;
import xzheng2.cmu.edu.hw3.Model.Run;
import xzheng2.cmu.edu.hw3.Model.RunLoader;
import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.Run.LocationReceiver;
import xzheng2.cmu.edu.hw3.Run.RunManager;
import xzheng2.cmu.edu.hw3.ViewActivity.RunMapActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment {
    private static final String TAG = "RunFragment";
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_RUN = 0;
    private static final int LOAD_LOCATION = 1;
    private Run mRun;
    private RunManager mRunManager;
    private Location mLastLocation;

    private Button mStartButton, mStopButton;
    private TextView mStartedTextView, mDurationTextView;
    private TextView mLatitudeTextView, mLongitudeTextView, mAltitudeTextView;
    private Button mMapButton;


    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

        protected void onLocationReceived(Context context, Location loc) {
            if (!mRunManager.isTrackingRun(mRun))
                return;
            mLastLocation = loc;
            if (isVisible())
                updateUI();
        }
    };

    protected void onProviderEnabledChanged(boolean enabled) {
        String gpsEnabled = enabled ? "Enabled" : "Not enabled";
        Toast.makeText(getActivity(), gpsEnabled, Toast.LENGTH_SHORT).show();
    }


    public static RunFragment newInstance(long runId) {
        // Required empty public constructor
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunFragment runFragment = new RunFragment();
        runFragment.setArguments(args);
        return runFragment;
    }

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
        mRunManager = RunManager.get(getActivity());

        Bundle args = getArguments();
        if (args != null) {
            long runId = args.getLong(ARG_RUN_ID, -1);
            if (runId != -1) {
                android.support.v4.app.LoaderManager loadManager = getLoaderManager();
                loadManager.initLoader(LOAD_RUN, args, new RunLoaderCallbacks());

//                LoaderManager loadManager = getLoaderManager();
                loadManager.initLoader(LOAD_RUN, args, new RunLoaderCallbacks());
                loadManager.initLoader(LOAD_LOCATION, args, new LocationLoaderCallbacks());
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_run, container, false);

        mStartedTextView = (TextView) view.findViewById(R.id.run_startedTextView);
        mDurationTextView = (TextView) view.findViewById(R.id.run_durationTextView);
        mStartButton = (Button) view.findViewById(R.id.run_startButton);
        mStopButton = (Button) view.findViewById(R.id.run_stopButton);
        mMapButton = (Button) view.findViewById(R.id.run_mapButton);
        mLatitudeTextView = (TextView) view.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView = (TextView) view.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView = (TextView) view.findViewById(R.id.run_altitudeTextView);

        //start
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRun == null) {
                    mRun = mRunManager.startNewRun();
//                    mStopButton.setEnabled(false);
                } else {
                    mRunManager.startTrackingRun(mRun);
//                    mStartButton.setEnabled(false); // cannot type now
//                    mStopButton.setEnabled(true);
                }
                updateUI();
            }
        });


        //end
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRunManager.stopRun();
//                mStopButton.setEnabled(false);
//                mStartButton.setEnabled(true);
//
//                Intent intent = new Intent(getActivity(), RunMapActivity.class);
//                intent.putExtra(RunMapActivity.EXTRA_RUN_ID, mRun.getId());
//                Log.d("****getId()" , ""  + mRun.getId());
//                startActivity(intent);

                updateUI();
            }
        });

        //map
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RunMapActivity.class);
                if (mRun == null) {
                    intent.putExtra(RunMapActivity.EXTRA_RUN_ID, -1);

                } else {
                    intent.putExtra(RunMapActivity.EXTRA_RUN_ID, mRun.getId());
                }
                startActivity(intent);
            }
        });

        updateUI();

        return view;
    }


    private class RunLoaderCallbacks implements LoaderManager.LoaderCallbacks<Run> {
        @Override
        public android.support.v4.content.Loader<Run> onCreateLoader(int id, Bundle args) {
            return new RunLoader(getActivity(), args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<Run> loader, Run run) {
            mRun = run;
            updateUI();
        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<Run> loader) {

        }
    }

    private class LocationLoaderCallbacks implements LoaderManager.LoaderCallbacks<Location> {
        @Override
        public android.support.v4.content.Loader<Location> onCreateLoader(int id, Bundle bundle) {
            return new LastLocationLoader(getActivity(), bundle.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<Location> loader, Location location) {
            mLastLocation = location;
            updateUI();
        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<Location> loader) {

        }
    }

    private void updateUI() {
        boolean started = mRunManager.isTrackingRun();
        boolean trackingThisRun = mRunManager.isTrackingRun(mRun);

        if (mRun != null) {
            mStartedTextView.setText(mRun.getStartDate().toString());
        }

        int duration = 0;
        if (mRun != null && mLastLocation != null) {
            duration = mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
            mStopButton.setEnabled(true);
        } else {
            mMapButton.setEnabled(false);
        }
        mDurationTextView.setText(Run.formatDuration(duration));

        mStartButton.setEnabled(!started);
        mStopButton.setEnabled(started && trackingThisRun);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver,
                new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }


}
