package xzheng2.cmu.edu.hw3.ViewActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.location.Location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;

//import xzheng2.cmu.edu.hw3.Manifest;
import xzheng2.cmu.edu.hw3.Model.LocationCursor;
import xzheng2.cmu.edu.hw3.Model.LocationListCursorLoader;
import xzheng2.cmu.edu.hw3.R;

public class MyRunMapActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {

    public static final String EXTRA_RUN_ID = "RUN_ID";
    private static final int LOAD_LOCATIONS = 0;

    private boolean mPermissionDenied = false;


    private GoogleMap googleMap;
    private LocationCursor mLocationCursor;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private long runId;


    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);
        setContentView(R.layout.activity_my_run_map);

        runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (runId != -1) {
            LoaderManager lm = getSupportLoaderManager(); //right????
            lm.initLoader(LOAD_LOCATIONS, args, this);
        }

        mapFragment.getMapAsync(this);


        //two fragment one to set dur
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d("onCreateLoader", "" + runId);

        return new LocationListCursorLoader(this, runId);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLocationCursor = (LocationCursor) cursor;
        updateUI();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLocationCursor.close();
        mLocationCursor = null;
    }

    @Override
    public void onMapReady(GoogleMap map) {


        this.googleMap = map;
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    private void updateUI() {
        if (googleMap == null || mLocationCursor == null)
            return;

        PolylineOptions line = new PolylineOptions();
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        // iterate over the locations
        mLocationCursor.moveToFirst();
        while (!mLocationCursor.isAfterLast()) {
            Location loc = mLocationCursor.getLocation();
            System.out.println("***latitude" + loc.getLatitude());
            System.out.println("***longitude" + loc.getLongitude());


            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            Log.d("MyRunMapActivity", "**UI");

            // if this is the first location, add a marker for it
            if (mLocationCursor.isFirst()) {
                String startDate = new Date(loc.getTime()).toString();
                MarkerOptions startMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.run_start))
                        .snippet(getResources().getString(R.string.run_started_at_format, startDate));
                googleMap.addMarker(startMarkerOptions);
            } else if (mLocationCursor.isLast()) {
                // if this is the last location, and not also the first, add a marker
                String endDate = new Date(loc.getTime()).toString();
                MarkerOptions finishMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.run_finish))
                        .snippet(getResources().getString(R.string.run_finished_at_format, endDate));
                googleMap.addMarker(finishMarkerOptions);
            }

            System.out.println("***latLng" + latLng);
            line.add(latLng);
            latLngBuilder.include(latLng);
            mLocationCursor.moveToNext();
        }

        // add the polyline to the map
        googleMap.addPolyline(line);

        Display display = this.getWindowManager().getDefaultDisplay();

        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(),
                display.getWidth(), display.getHeight(), 15);

        googleMap.moveCamera(movement);
    }
}
