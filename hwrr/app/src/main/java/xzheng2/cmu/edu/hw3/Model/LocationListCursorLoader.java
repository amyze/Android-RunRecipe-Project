package xzheng2.cmu.edu.hw3.Model;

import android.content.Context;
import android.database.Cursor;

public class LocationListCursorLoader extends SQLiteCursorLoader {
    private long mReportId;
    
    public LocationListCursorLoader(Context c, long reportId) {
        super(c);
        mReportId = reportId;
    }

    @Override
    protected Cursor loadCursor() {
        return ReportManager.get(getContext()).queryLocationsForReport(mReportId);
    }
}