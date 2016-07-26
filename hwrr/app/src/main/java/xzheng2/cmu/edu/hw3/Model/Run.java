package xzheng2.cmu.edu.hw3.Model;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by chengcheng on 7/26/16.
 */
public class Run {
    private long mId;
    private Date mStartDate;
//    private Date mStart;
//    private Date mRun;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public int getDurationSeconds(long endMillis) {
        return (int)((endMillis - mStartDate.getTime()) / 1000);
    }

    public Run() {
        mId = -1;
        mStartDate = new Date();
    }

    public static String formatDuration(int durationSeconds) {
        int seconds = durationSeconds % 60;
        int minutes = ((durationSeconds - seconds) / 60) % 60;
        int hours = (durationSeconds - minutes * 60 - seconds) / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


}
