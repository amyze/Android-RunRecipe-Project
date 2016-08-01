package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/31/16.
 */
import android.content.Context;
import android.location.Location;

public class LastLocationLoader extends DataLoader<Location> {
    private long runId;

    public LastLocationLoader(Context context, long Id) {
        super(context);
        runId = Id;
    }

    @Override
    public Location loadInBackground() {
        return RunManager.get(getContext()).getLastLocationForRun(runId);
    }
}