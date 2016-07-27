package xzheng2.cmu.edu.hw3.Run;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import hugo.weaving.DebugLog;
import xzheng2.cmu.edu.hw3.Model.LocationCursor;
import xzheng2.cmu.edu.hw3.Model.Run;
import xzheng2.cmu.edu.hw3.Model.RunCursor;
import xzheng2.cmu.edu.hw3.Model.RunDatabaseHelper;

/**
 * Created by chengcheng on 7/26/16.
 */
public class RunManager {
    private static final String TAG = "runManager";

    private static final String PREFS_FILE = "run";

    private static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";

//    public static final String ACTION_LOCATION = "com.donnemartin.android.fieldreporter.ACTION_LOCATION";
    public static final String ACTION_LOCATION = "xzheng2.cmu.edu.hw3.ACTION_LOCATION"; //?
    private static final String TEST_PROVIDER = "TEST_PROVIDER";

    private static RunManager sRunManager;
    private LocationManager mLocationManager;
    private Context mAppContext;
    private RunDatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentRunId;

    @DebugLog
    private RunManager(Context appContext) {
        mAppContext = appContext;
        mHelper = new RunDatabaseHelper(mAppContext);
        mLocationManager = (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);
    }

    public  static RunManager get(Context context) {
        if (sRunManager == null) {
            sRunManager = new RunManager(context.getApplicationContext());
        }
        return sRunManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent intent = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, intent, flags);
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;
        if (mLocationManager.getProvider(TEST_PROVIDER) != null
                && mLocationManager.isProviderEnabled(TEST_PROVIDER)) {
            provider = TEST_PROVIDER;
        }
        Log.d(TAG, "Using provider: " + provider);

        Location lastLocation = mLocationManager.getLastKnownLocation(provider);
        if (lastLocation != null) {
            lastLocation.setTime(System.currentTimeMillis());
            broadcastLocation(lastLocation);
        }

        PendingIntent pendingIntent = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, pendingIntent);
    }

    public void stopLocationUpdates() {
        PendingIntent pendingIntent = getLocationPendingIntent(false);
        if (pendingIntent != null) {
            mLocationManager.removeUpdates(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }

    public boolean isTrackingRun(Run run) {
        return run != null && run.getId() == mCurrentRunId;
    }

    private void broadcastLocation(Location location) {
        Intent intent = new Intent(ACTION_LOCATION);
        intent.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        mAppContext.sendBroadcast(intent);
    }

    public Run startNewRun() {
        Run run = insertRun();
        startTrackingRun(run);
        return run;
    }

    public void startTrackingRun(Run run) {
        mCurrentRunId = run.getId();
        mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId);
        startLocationUpdates();
    }

    public void stopRun() {
        startLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
    }

    private Run insertRun() { // create a new run
        Run run = new Run();
        run.setId(mHelper.insertRun(run)); // helper is kind of
        return run;
    }

    public RunCursor queryRuns() {
        return mHelper.queryRuns();
    }

    public Run getRun(long id) {
        Run run = null;
        RunCursor cursor = mHelper.queryRun(id);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            run = cursor.getRun();
        }
        cursor.close();
        return run;
    }

    public Location getLastLocationForRun(long runId) {
        Location location = null;
        LocationCursor cursor = mHelper.queryLastLocationForRun(runId);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
            location = cursor.getLocation();
        cursor.close();
        return location;
    }

    public Cursor queryLocationsForRun(long mRunId) {
        return mHelper.queryLocationsForRun(mRunId);
    }
}
