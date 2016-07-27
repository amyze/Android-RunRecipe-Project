package xzheng2.cmu.edu.hw3.Model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

/**
 * Created by chengcheng on 7/26/16.
 */
public class RunCursor extends CursorWrapper {
    private String COLUMN_RUN_ID = "_id";
    private String COLUMN_RUN_START_DATE = "start_date";
    private static final String COLUMN_RUN_END_DATE = "end_date";
    private static final String COLUMN_RUN_DURATION = "duration";

    public RunCursor(Cursor c) {
        super(c);
    }

    public Run getRun() {
        if (isBeforeFirst() || isAfterLast()) {
            return null;
        }
        Run run = new Run();
        run.setId(getColumnIndex(COLUMN_RUN_ID));
        run.setStartDate(new Date(getLong(getColumnIndex(COLUMN_RUN_START_DATE))));
        return run;
    }
}
