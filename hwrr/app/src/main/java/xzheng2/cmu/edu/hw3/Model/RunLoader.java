package xzheng2.cmu.edu.hw3.Model;

//import android.app.Activity;
//import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
//import android.content.Loader;

import xzheng2.cmu.edu.hw3.Run.RunManager;

/**
 * Created by chengcheng on 7/26/16.
 */
public class RunLoader extends AsyncTaskLoader<Run> {
    private long mRunId;
    public RunLoader(Context context, long id) {
        super(context);
        mRunId = id;
    }

    public Run loadInBackground() {
        return RunManager.get(getContext()).getRun(mRunId);
    }
}
