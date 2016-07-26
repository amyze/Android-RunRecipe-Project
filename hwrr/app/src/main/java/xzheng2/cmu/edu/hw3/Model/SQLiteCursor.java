package xzheng2.cmu.edu.hw3.Model;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by chengcheng on 7/26/16.
 */
public abstract  class SQLiteCursor extends AsyncTaskLoader<Cursor> {
    private Cursor mCursor;
    public SQLiteCursor(Context context) {
        super(context);
    }
    protected abstract Cursor loadCursor();

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursor();
        if (cursor != null) {
            // ensure that the content window is filled
            cursor.getCount();
        }
        return cursor;
    }

    @Override
    public void deliverResult(Cursor data) {
        Cursor preCursor = mCursor;
        mCursor = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (preCursor != null && preCursor != data && !preCursor.isClosed()) {
            preCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }
}
