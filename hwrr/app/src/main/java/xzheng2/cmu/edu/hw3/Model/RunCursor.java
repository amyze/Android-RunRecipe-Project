package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/31/16.
 */
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

public class RunCursor extends CursorWrapper {
    private static final String COLUMN_RUN_ID = "_id";
    private static final String COLUMN_RUN_START_DATE = "start_date";
    private static final String COLUMN_RUN_STOP_DATE = "stop_date";
    private static final String COLUMN_RUN_DURATION = "duration";

    public RunCursor(Cursor c) {
        super(c);
    }

    public Run getRun() {
        if (isBeforeFirst() || isAfterLast())
            return null;
        Run run = new Run();
        run.setId(getLong(getColumnIndex(COLUMN_RUN_ID)));
        run.setStartDate(new Date(getLong(getColumnIndex(COLUMN_RUN_START_DATE))));
        run.setStopDate(new Date(getLong(getColumnIndex(COLUMN_RUN_STOP_DATE))));
        run.setDuration(getColumnIndex(COLUMN_RUN_DURATION));
        return run;
    }
}