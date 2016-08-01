package xzheng2.cmu.edu.hw3.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import xzheng2.cmu.edu.hw3.Model.LastLocationLoader;
import xzheng2.cmu.edu.hw3.Model.LocationReceiver;
import xzheng2.cmu.edu.hw3.Model.Run;
import xzheng2.cmu.edu.hw3.Model.RunLoader;
import xzheng2.cmu.edu.hw3.Model.RunManager;
import xzheng2.cmu.edu.hw3.R;
import xzheng2.cmu.edu.hw3.ViewActivity.StopRunActivity;


public class RunFragment extends Fragment {
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_RUN = 0;
    private static final int LOAD_LOCATION = 1;

    private RunManager runManager;

    private Run run;
    private Location lastLocation;

    private Button startButton;
    private Button stopButton;
    private TextView startedTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView durationTextView;
    
    private BroadcastReceiver locationReceiver = new LocationReceiver() {

        @Override
        protected void onLocationReceived(Context context, Location loc) {
            if (!runManager.isTrackingRun(run))
                return;
            lastLocation = loc;
            if (isVisible()) 
                updateUI();
        }
        
        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
        }
    };
    

    
    public static RunFragment newInstance(long runId) {
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_RUN_ID, runId);
        RunFragment rf = new RunFragment();
        rf.setArguments(bundle);
        return rf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        runManager = RunManager.get(getActivity());

        Bundle args = getArguments();
        if (args != null) {
            long reportId = args.getLong(ARG_RUN_ID, -1);
            if (reportId != -1) {
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_RUN, args, new ReportLoaderCallbacks());
                lm.initLoader(LOAD_LOCATION, args, new LocationLoaderCallbacks());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        
        startedTextView = (TextView)view.findViewById(R.id.run_startedTextView);
        latitudeTextView = (TextView)view.findViewById(R.id.run_latitudeTextView);
        longitudeTextView = (TextView)view.findViewById(R.id.run_longitudeTextView);
        durationTextView = (TextView)view.findViewById(R.id.run_durationTextView);
        
        startButton = (Button)view.findViewById(R.id.run_startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (run == null) {
                    run = runManager.startNewRun(); //initialize report
                    Log.d("start!!!", "" + run.getId());
                }
                updateUI();
            }
        });
        
        stopButton = (Button)view.findViewById(R.id.run_stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runManager.stopRun();
                Intent i = new Intent(getActivity(), StopRunActivity.class);
                i.putExtra(StopRunActivity.EXTRA_RUN_ID, run.getId());
                startActivity(i);
                updateUI();
                // finish current run
                run = null;
                RefreshUI();
            }
        });

        updateUI();
        
        return view;
    }

    private void RefreshUI() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        longitudeTextView.setText("");
        latitudeTextView.setText("");
        startedTextView.setText("");
        durationTextView.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(locationReceiver,
                new IntentFilter(RunManager.ACTION_LOCATION));
    }
    
    @Override
    public void onStop() {
        getActivity().unregisterReceiver(locationReceiver);
        super.onStop();
    }
    
    private void updateUI() {
        boolean started = runManager.isTrackingRun();
        boolean trackingThisReport = runManager.isTrackingRun(run);
        
        if (run != null)
            startedTextView.setText(run.getStartDate().toString());
        
        int durationSeconds = 0;
        if (run != null && lastLocation != null) {
            durationSeconds = run.getDurationSeconds(lastLocation.getTime());
            latitudeTextView.setText(Double.toString(lastLocation.getLatitude()));
            longitudeTextView.setText(Double.toString(lastLocation.getLongitude()));
        }

        durationTextView.setText(Run.formatDuration(durationSeconds));
        startButton.setEnabled(!started);
        stopButton.setEnabled(started && trackingThisReport);
    }
    
    private class ReportLoaderCallbacks implements LoaderCallbacks<Run> {
        
        @Override
        public Loader<Run> onCreateLoader(int id, Bundle args) {
            return new RunLoader(getActivity(), args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Run> loader, Run run) {
            RunFragment.this.run = run;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Run> loader) {
        }
    }

    private class LocationLoaderCallbacks implements LoaderCallbacks<Location> {
        
        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {
            return new LastLocationLoader(getActivity(), args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location location) {
            lastLocation = location;
            updateUI();
        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {
        }
    }
}
