package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/31/16.
 */
import android.content.Context;
import android.database.Cursor;

public class LocationListCursorLoader extends SQLiteCursorLoader {
    private long runId;

    public LocationListCursorLoader(Context c, long id) {
        super(c);
        runId = id;
    }

    @Override
    protected Cursor loadCursor() {
        return RunManager.get(getContext()).queryLocationsForRun(runId);
    }
}