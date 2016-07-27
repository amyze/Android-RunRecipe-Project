package xzheng2.cmu.edu.hw3.View;


import android.database.Cursor;
import android.icu.util.BuddhistCalendar;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.PrivateKey;
import java.util.Date;

import xzheng2.cmu.edu.hw3.Model.LocationCursor;
import xzheng2.cmu.edu.hw3.Model.LocationListCursorLoader;
import xzheng2.cmu.edu.hw3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunMapFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback {
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_LOCATIONS = 0;

    private GoogleMap googleMap;
    private LocationCursor locationCursor;

    public static RunMapFragment newInstance(long runId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunMapFragment fragment = new RunMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RunMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            long runId = args.getLong(ARG_RUN_ID, -1);
            if (runId != -1) {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(LOAD_LOCATIONS, args, this);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_run_map, container, false);
        View view = super.onCreateView(inflater, parent, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LocationListCursorLoader(getActivity(), args.getLong(ARG_RUN_ID, -1));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        locationCursor = (LocationCursor) cursor;
        updateUI();

    }

    private void updateUI() {
        if (googleMap == null || locationCursor == null) {
            return;
        }

        PolylineOptions line = new PolylineOptions();
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        locationCursor.moveToFirst();
        while (!locationCursor.isAfterLast()) {
            Location loc = locationCursor.getLocation();
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            if (locationCursor.isFirst()) {
                String startDate = new Date(loc.getTime()).toString();
                MarkerOptions startMaker = new MarkerOptions()
                        .position(latLng)
                        .title("Run start")
                        .snippet(getResources().getString(R.string.report_started_at_format, startDate));
                googleMap.addMarker(startMaker);
            } else if (locationCursor.isLast()) {
                String endDate = new Date(loc.getTime()).toString();
                MarkerOptions stopMarker = new MarkerOptions()
                        .position(latLng)
                        .title("Run stop")
                        .snippet(getResources().getString(R.string.report_finished_at_format, endDate));
                googleMap.addMarker(stopMarker);
            }

            line.add(latLng);
            latLngBuilder.include(latLng);
            locationCursor.moveToNext();
        }
        googleMap.addPolyline(line);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(),
                display.getWidth(), display.getHeight(), 15);
        googleMap.moveCamera(movement);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        locationCursor.close();
        locationCursor = null;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
