package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/28/16.
 */
import android.content.Context;

class ReportLoader extends DataLoader<Report> {
    private long mReportId;

    public ReportLoader(Context context, long reportId) {
        super(context);
        mReportId = reportId;
    }

    @Override
    public Report loadInBackground() {
        return ReportManager.get(getContext()).getReport(mReportId);
    }
}