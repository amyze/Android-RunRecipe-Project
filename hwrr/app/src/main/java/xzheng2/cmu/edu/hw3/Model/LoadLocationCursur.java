package xzheng2.cmu.edu.hw3.Model;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by chengcheng on 7/26/16.
 */
public class LoadLocationCursur extends SQLiteCursor {
    private long mRunId;

    public LoadLocationCursur(Context context, long runId) {
        super(context);
        mRunId = runId;
    }

    @Override
    protected Cursor loadCursor() {
        return null;
    } //get list of location based on runID


}
