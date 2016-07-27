package xzheng2.cmu.edu.hw3.Model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import xzheng2.cmu.edu.hw3.Run.RunManager;

/**
 * Created by chengcheng on 7/26/16.
 */
public class LocationListCursorLoader extends SQLiteCursor {
    private long mRunId;

    public LocationListCursorLoader(Activity activity, long id) {
        super(activity);
        mRunId = id;
    }

    @Override
    protected Cursor loadCursor() {
        return RunManager.get(getContext()).queryLocationsForRun(mRunId);
    }
}
