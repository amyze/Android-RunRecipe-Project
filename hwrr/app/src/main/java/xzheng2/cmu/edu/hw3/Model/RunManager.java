package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/31/16.
 */
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.Date;

public class RunManager {
    private static final String TAG = "RunManager";

    private static final String PREFS_FILE = "reports";
    private static final String PREF_CURRENT_RUN_ID = "RunManager.currentReportId";

    public static final String ACTION_LOCATION = "com.donnemartin.android.fieldreporter.ACTION_LOCATION";

    private static final String TEST_PROVIDER = "TEST_PROVIDER";

    private static RunManager runManager;
    private Context appContext;
    private LocationManager locationManager;
    private RunDatabaseHelper runDatabaseHelper;
    private SharedPreferences prefs; //
    private long currentRunId;

    private RunManager(Context appContext) {
        this.appContext = appContext;
        locationManager = (LocationManager) this.appContext.getSystemService(Context.LOCATION_SERVICE);
        runDatabaseHelper = new RunDatabaseHelper(this.appContext);
        prefs = this.appContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        currentRunId = prefs.getLong(PREF_CURRENT_RUN_ID, -1);
    }

    public static RunManager get(Context c) {
        if (runManager == null) {
            // we use the application context to avoid leaking activities
            runManager = new RunManager(c.getApplicationContext());
        }
        return runManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(appContext, 0, broadcast, flags);
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;
        // if we have the test provider and it's enabled, use it
        if (locationManager.getProvider(TEST_PROVIDER) != null &&
                locationManager.isProviderEnabled(TEST_PROVIDER)) {
            provider = TEST_PROVIDER;
        }

        Location lastKnown = locationManager.getLastKnownLocation(provider);

        Log.d("LocationUpdatesLat", "" + lastKnown.getLatitude());
        Log.d("LocationUpdatesLong", "" + lastKnown.getLongitude());


        if (lastKnown != null) {
            lastKnown.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnown);
        }
        PendingIntent pi = getLocationPendingIntent(true);
        locationManager.requestLocationUpdates(provider, 0, 0, pi);
        locationManager.requestLocationUpdates(provider, 0, 1, pi);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            locationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }

    public boolean isTrackingRun(Run run) {
        return run != null && run.getId() == currentRunId;
    }

    private void broadcastLocation(Location location) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        appContext.sendBroadcast(broadcast);
    }

    public Run startNewRun() {
        Run run = insertRun();
        startTrackingRun(run);
        return run;
    }

    public void startTrackingRun(Run run) {
        // keep the ID
        currentRunId = run.getId();
        // store it in shared preferences
        prefs.edit().putLong(PREF_CURRENT_RUN_ID, currentRunId).commit();
        // start location updates
        startLocationUpdates();
    }

    public void stopRun() {
        stopLocationUpdates();
        Log.d("ReportManager_stop!!", "" + currentRunId);

        updateRun(currentRunId, new Date());
        currentRunId = -1;
        prefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
    }

    private Run insertRun() {
        Run run = new Run();
        run.setId(runDatabaseHelper.insertRun(run));
        return run;
    }

    public RunCursor queryRuns() {
        return runDatabaseHelper.queryRuns();
    }


    public Run getRun(long id) {
        Run run = null;
        RunCursor cursor = runDatabaseHelper.queryRun(id);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
            run = cursor.getRun();
        cursor.close();
        return run;
    }

    public void updateRun(long runId, Date stop) {
        Run run = getRun(runId);
        Log.d("ManagerupdateRun", "" + run.getId());
        run.setStopDate(stop);
        run.setDuration(stop.getTime());

        Log.d("RunManager,Start", run.getStartDate().toString());
        Log.d("RunManager,Stop", run.getStopDate().toString());
        Log.d("RunManager,Duration", "" + run.getDuration());
        runDatabaseHelper.updateRun(run);
    }

    public void insertLocation(Location loc) {
        if (currentRunId != -1) {
            runDatabaseHelper.insertLocation(currentRunId, loc);
        } else {
            Log.e(TAG, "Location received with no tracking run; ignoring.");
        }
    }

    public Location getLastLocationForRun(long reportId) {
        Location location = null;
        LocationCursor cursor = runDatabaseHelper.queryLastLocationForRun(reportId);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
            location = cursor.getLocation();
        cursor.close();
        return location;
    }

    public LocationCursor queryLocationsForRun(long reportId) {
        return runDatabaseHelper.queryLocationsForRun(reportId);
    }
}

