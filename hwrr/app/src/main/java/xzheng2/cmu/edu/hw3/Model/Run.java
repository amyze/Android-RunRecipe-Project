package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/31/16.
 */
import java.util.Date;

public class Run {
    private long id;
    private Date startDate;
    private Date stopDate;
    private int duration;

    public Run() {
        id = -1;
        startDate = new Date();
    }
    public Date getStopDate() {
        return stopDate;
    }

    public void  setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getDurationSeconds(long endMillis) {
        return (int)((endMillis - startDate.getTime()) / 1000);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(long endMillis) {
        duration = getDurationSeconds(endMillis);
    }

    public static String formatDuration(int durationSeconds) {
        int seconds = durationSeconds % 60;
        int minutes = ((durationSeconds - seconds) / 60) % 60;
        int hours = (durationSeconds - (minutes * 60) - seconds) / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}

