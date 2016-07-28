package xzheng2.cmu.edu.hw3.Model;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.location.Location;

/**
 * Created by chengcheng on 7/26/16.
 */
public class LocationCursor extends CursorWrapper {
    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_ALTITUDE = "altitude";
    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOCATION_PROVIDER = "provider";

    public LocationCursor(Cursor c) {
        super(c);
    }

    public Location getLocation() {
        if (isBeforeFirst() || isAfterLast())
            return null;
        // first get the provider out so we can use the constructor
        String provider = getString(getColumnIndex(COLUMN_LOCATION_PROVIDER));
        Location loc = new Location(provider);
        // populate the remaining properties
        loc.setLongitude(getDouble(getColumnIndex(COLUMN_LOCATION_LONGITUDE)));
        loc.setLatitude(getDouble(getColumnIndex(COLUMN_LOCATION_LATITUDE)));
        loc.setAltitude(getDouble(getColumnIndex(COLUMN_LOCATION_ALTITUDE)));
        loc.setTime(getLong(getColumnIndex(COLUMN_LOCATION_TIMESTAMP)));
        return loc;
    }
}
