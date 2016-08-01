package xzheng2.cmu.edu.hw3.View;

import java.util.Date;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import xzheng2.cmu.edu.hw3.Model.LocationCursor;
import xzheng2.cmu.edu.hw3.Model.LocationListCursorLoader;
import xzheng2.cmu.edu.hw3.R;


public class RunMapFragment extends SupportMapFragment implements LoaderCallbacks<Cursor>, OnMapReadyCallback {
    private static final String ARG_RUN_ID = "REPORT_ID";
    private static final int LOAD_LOCATIONS = 0;

    private GoogleMap googleMap;
    private LocationCursor locationCursor;

    public static RunMapFragment newInstance(long runId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunMapFragment rf = new RunMapFragment();
        rf.setArguments(args);
        return rf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            long reportId = args.getLong(ARG_RUN_ID, -1);
            if (reportId != -1) {
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_LOCATIONS, args, this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        getMapAsync(this);
        return v;
    }

    private void updateUI() {
        if (googleMap == null || locationCursor == null)
            return;

        PolylineOptions line = new PolylineOptions();
        // also create a LatLngBounds so we can zoom to fit
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        // iterate over the locations
//        Log.d("locationCursorCount", "" + locationCursor.getCount());
//        if (locationCursor.getCount() > 0) {
            locationCursor.moveToFirst();
//        } else {
//            return;
//        }



        Log.d("***Lng", "" + locationCursor.getLocation().getLongitude());
        Log.d("***Lat", "" + locationCursor.getLocation().getLatitude());


        while (!locationCursor.isAfterLast()) {
            Location loc = locationCursor.getLocation();
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            // if this is the first location, add a marker for it
            if (locationCursor.isFirst()) {
                String startDate = new Date(loc.getTime()).toString();
                MarkerOptions startMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.run_start))
                        .snippet(getResources().getString(R.string.run_started_at_format, startDate));
                googleMap.addMarker(startMarkerOptions);
            } else if (locationCursor.isLast()) {
                // if this is the last location, and not also the first, add a marker
                String endDate = new Date(loc.getTime()).toString();
                MarkerOptions finishMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.run_finish))
                        .snippet(getResources().getString(R.string.run_finished_at_format, endDate));
                googleMap.addMarker(finishMarkerOptions);
            }

            line.add(latLng);
            latLngBuilder.include(latLng);
            locationCursor.moveToNext();
        }
        // add the polyline to the map
        googleMap.addPolyline(line);
        // make the map zoom to show the track, with some padding
        // use the size of the current display in pixels as a bounding box
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        // construct a movement instruction for the map camera
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(),
                display.getWidth(), display.getHeight(), 15);
        googleMap.moveCamera(movement);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> newLoader = new LocationListCursorLoader(getActivity(), args.getLong(ARG_RUN_ID, -1));
        Log.d("newLoader", newLoader.toString());
        return newLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("onLoadFinished", cursor.toString());
        locationCursor = (LocationCursor) cursor;
        updateUI();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // stop using the data
        locationCursor.close();
        locationCursor = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}

