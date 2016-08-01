package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/31/16.
 */
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;


public class RunDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "reports.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_REPORT = "report";
    private static final String COLUMN_RUN_ID = "_id";
    private static final String COLUMN_RUN_START_DATE = "start_date";
    private static final String COLUMN_RUN_STOP_DATE = "stop_date";
    private static final String COLUMN_RUN_DURATION = "duration";


    private static final String TABLE_LOCATION = "location";
    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_ALTITUDE = "altitude";
    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOCATION_PROVIDER = "provider";
    private static final String COLUMN_LOCATION_RUN_ID = "report_id";

    public RunDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table report (_id integer primary key autoincrement, start_date integer," +
                " stop_date integer," +
                " duration integer)");

        // create the "location" table
        db.execSQL("create table location (" +
                " timestamp integer, latitude real, longitude real, altitude real," +
                " provider varchar(100), report_id integer references report(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // implement schema changes and data massage here when upgrading
    }

    public long insertRun(Run run) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_REPORT, null, cv);
    }

    public void updateRun(Run run) {
        ContentValues cv = new ContentValues();
        Date stop = new Date();
        cv.put(COLUMN_RUN_STOP_DATE, stop.getTime());
        cv.put(COLUMN_RUN_DURATION, run.getDuration());
        cv.put(COLUMN_RUN_ID, run.getId());
        String[] whereArgs = new String[]{Long.toString(run.getId())};

        Log.d("HelperStart", "" + run.getStartDate());
        Log.d("HelperStop", "" + run.getStopDate());
        Log.d("HelperDuration", "" + run.getDuration());
        Log.d("Helper", cv.toString());

        int result = getWritableDatabase().update(TABLE_REPORT, cv, "_id=?", whereArgs);

        Log.d("HelperDuration", ":result" + result);

    }

    public long insertLocation(long runId, Location location) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());
        cv.put(COLUMN_LOCATION_ALTITUDE, location.getAltitude());
        cv.put(COLUMN_LOCATION_TIMESTAMP, location.getTime());
        cv.put(COLUMN_LOCATION_PROVIDER, location.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID, runId);
        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }

    public RunCursor queryRuns() {
        Cursor wrapped = getReadableDatabase().query(TABLE_REPORT,
                null, null, null, null, null, COLUMN_RUN_START_DATE + " asc");
        return new RunCursor(wrapped);
    }

    public RunCursor queryRun(long id) {
        Cursor wrapped = getReadableDatabase().query(TABLE_REPORT,
                null, // all columns
                COLUMN_RUN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, // group by
                null, // order by
                null, // having
                "1"); // limit 1 row
        return new RunCursor(wrapped);
    }

    public LocationCursor queryLastLocationForRun(long runId) {
        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
                null, // all columns
                COLUMN_LOCATION_RUN_ID + " = ?",
                new String[]{String.valueOf(runId)},
                null, // group by
                null, // having
                COLUMN_LOCATION_TIMESTAMP + " desc", // order by latest first
                "1"); // limit 1
        return new LocationCursor(wrapped);
    }

    public LocationCursor queryLocationsForRun(long runId) {
        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
                null,
                COLUMN_LOCATION_RUN_ID + " = ?",
                new String[]{String.valueOf(runId)},
                null, // group by
                null, // having
                COLUMN_LOCATION_TIMESTAMP + " asc"); // order by timestamp
        return new LocationCursor(wrapped);
    }

}