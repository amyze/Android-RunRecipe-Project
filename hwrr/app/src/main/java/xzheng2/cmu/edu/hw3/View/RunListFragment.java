package xzheng2.cmu.edu.hw3.View;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import xzheng2.cmu.edu.hw3.Model.Run;
import xzheng2.cmu.edu.hw3.Model.RunCursor;
import xzheng2.cmu.edu.hw3.Model.RunManager;
import xzheng2.cmu.edu.hw3.Model.SQLiteCursorLoader;
import xzheng2.cmu.edu.hw3.ViewActivity.RunHistoryActivity;


public class RunListFragment extends ListFragment implements LoaderCallbacks<Cursor> {
    private static final int REQUEST_NEW_REPORT = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // initialize the loader to load the list of reports
        getLoaderManager().initLoader(0, null, this);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // we only ever load the reports, so assume this is the case
        return new ReportListCursorLoader(getActivity());
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // create an adapter to point at this cursor
        RunCursorAdapter adapter = new RunCursorAdapter(getActivity(), (RunCursor)cursor);
        setListAdapter(adapter);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // stop using the cursor (via the adapter)
        setListAdapter(null);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.report_list_options, menu);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_NEW_REPORT == requestCode) {
            // restart the loader to get any new report available
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), RunHistoryActivity.class);
        intent.putExtra(RunHistoryActivity.EXTRA_RUN_ID, id);
        startActivity(intent);
    }

    private static class ReportListCursorLoader extends SQLiteCursorLoader {

        public ReportListCursorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            return RunManager.get(getContext()).queryRuns();
        }
        
    }
    
    private static class RunCursorAdapter extends CursorAdapter {
        
        private RunCursor runCursor;
        
        public RunCursorAdapter(Context context, RunCursor cursor) {
            super(context, cursor, 0);
            runCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // use a layout inflater to get a row view
            LayoutInflater inflater = 
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Run run = runCursor.getRun();
            
            TextView startDateTextView = (TextView)view;
            startDateTextView.setText("Run at " + run.getStartDate());
        }
        
    }
}
