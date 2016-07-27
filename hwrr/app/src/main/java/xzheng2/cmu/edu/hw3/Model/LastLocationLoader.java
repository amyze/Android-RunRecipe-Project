package xzheng2.cmu.edu.hw3.Model;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.location.Location;

import xzheng2.cmu.edu.hw3.Run.RunManager;

/**
 * Created by chengcheng on 7/26/16.
 */
public class LastLocationLoader extends AsyncTaskLoader<Location> {
    private long mRunId;
    public LastLocationLoader(Context context, long id) {
        super(context);
        mRunId = id;
    }

    @Override
    public Location loadInBackground() {
        return RunManager.get(getContext()).getLastLocationForRun(mRunId);
    }
}
