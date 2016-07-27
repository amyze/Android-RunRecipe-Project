
package xzheng2.cmu.edu.hw3.ViewActivity;

import android.database.Cursor;
import android.location.Location;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.google.android.gms.maps.GoogleMap;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;

import xzheng2.cmu.edu.hw3.Model.LocationCursor;
import xzheng2.cmu.edu.hw3.Model.LocationListCursorLoader;
import xzheng2.cmu.edu.hw3.R;

public class RunMapActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {
    public static String EXTRA_RUN_ID = "RUN_ID";
    private static final int LOAD_LOCATIONS = 0;

    private GoogleMap googleMap;
    private LocationCursor mLocationCursor;
    private String ARG_RUN_ID;
    private boolean needToUPdateUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_map);

        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        if (runId == -1) {
            return;
        }
//        googleMap = getMap();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void updateUI() {

        if (googleMap == null || mLocationCursor == null)
            return;

        // set up an overlay on the map for this report's locations
        // create a polyline with all of the points
        PolylineOptions line = new PolylineOptions();
        // also create a LatLngBounds so we can zoom to fit
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        // iterate over the locations
        mLocationCursor.moveToFirst();
        while (!mLocationCursor.isAfterLast()) {
            Location loc = mLocationCursor.getLocation();
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            // if this is the first location, add a marker for it
            if (mLocationCursor.isFirst()) {
                String startDate = new Date(loc.getTime()).toString();
                MarkerOptions startMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.run_start))
                        .snippet(getResources().getString(R.string.report_started_at_format, startDate));
                googleMap.addMarker(startMarkerOptions);
            } else if (mLocationCursor.isLast()) {
                // if this is the last location, and not also the first, add a marker
                String endDate = new Date(loc.getTime()).toString();
                MarkerOptions finishMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.report_finish))
                        .snippet(getResources().getString(R.string.report_finished_at_format, endDate));
                googleMap.addMarker(finishMarkerOptions);
            }

            line.add(latLng);
            latLngBuilder.include(latLng);
            mLocationCursor.moveToNext();
        }
        // add the polyline to the map
        googleMap.addPolyline(line);
        // make the map zoom to show the track, with some padding
        // use the size of the current display in pixels as a bounding box
        Display display = getWindowManager().getDefaultDisplay();
        // construct a movement instruction for the map camera
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(),
                display.getWidth(), display.getHeight(), 15);
        googleMap.moveCamera(movement);

        needToUPdateUI = false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LocationListCursorLoader(this, Long.valueOf(EXTRA_RUN_ID));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLocationCursor = (LocationCursor) cursor;

        if (googleMap != null) {
            updateUI();
        } else {
            needToUPdateUI = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // stop using the data
        mLocationCursor.close();
        mLocationCursor = null;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        this.googleMap = map;
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        if (needToUPdateUI) {
            updateUI();
        }

    }
}
