package xzheng2.cmu.edu.hw3.Model;

//import android.content.AsyncTaskLoader;
import android.content.Context;
        import android.location.Location;

        import xzheng2.cmu.edu.hw3.Run.RunManager;

/**
 * Created by chengcheng on 7/26/16.
 */

class LastLocationLoader extends DataLoader<Location> {
    private long mRunId;

    public LastLocationLoader(Context context, long reportId) {
        super(context);
        mRunId = reportId;
    }

    @Override
    public Location loadInBackground() {
        return RunManager.get(getContext()).getLastLocationForReport(mRunId);
    }
}