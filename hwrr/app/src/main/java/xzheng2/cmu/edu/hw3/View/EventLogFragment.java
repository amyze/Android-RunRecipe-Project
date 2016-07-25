
package xzheng2.cmu.edu.hw3.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import xzheng2.cmu.edu.hw3.R;


public class EventLogFragment extends Fragment implements PlaceSelectionListener, OnMapReadyCallback {

    private GoogleMap googleMap;
    private Place latestPlace;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onPlaceSelected(Place place) {
        showItOnGoogleMap(place);
    }

    private void showItOnGoogleMap(Place place) {

        latestPlace = place;
        if (googleMap != null) {
            doShowItOnMap();
        }

    }

    private void doShowItOnMap() {
        googleMap.clear();
        LatLng latLng = latestPlace.getLatLng();
        String address = latestPlace.getAddress().toString();
        googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), null);
        latestPlace = null;
    }


    @Override
    public void onError(Status status) {
        Toast.makeText(getActivity(), "Place selection failed: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        if (latestPlace != null) {
            doShowItOnMap();
        } else {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Please search your address"));
        }


    }
}
