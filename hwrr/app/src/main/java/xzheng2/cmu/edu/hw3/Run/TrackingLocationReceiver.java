package xzheng2.cmu.edu.hw3.Run;

/**
 * Created by chengcheng on 7/27/16.
 */
import android.content.Context;
import android.location.Location;

public class TrackingLocationReceiver extends LocationReceiver {

    @Override
    protected void onLocationReceived(Context c, Location loc) {
        RunManager.get(c).insertLocation(loc);
    }

}