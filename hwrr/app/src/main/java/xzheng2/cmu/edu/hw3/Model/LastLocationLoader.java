package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/28/16.
 */
import android.content.Context;
import android.location.Location;

class LastLocationLoader extends DataLoader<Location> {
    private long mReportId;

    public LastLocationLoader(Context context, long reportId) {
        super(context);
        mReportId = reportId;
    }

    @Override
    public Location loadInBackground() {
        return ReportManager.get(getContext()).getLastLocationForReport(mReportId);
    }
}
