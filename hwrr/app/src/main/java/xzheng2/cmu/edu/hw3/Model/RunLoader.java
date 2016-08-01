package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by chengcheng on 7/31/16.
 */
import android.content.Context;

public class RunLoader extends DataLoader<Run> {
    private long runId;

    public RunLoader(Context context, long id) {
        super(context);
        runId = id;
    }

    @Override
    public Run loadInBackground() {
        return RunManager.get(getContext()).getRun(runId);
    }
}
