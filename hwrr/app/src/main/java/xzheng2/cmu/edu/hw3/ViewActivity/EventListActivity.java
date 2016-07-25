package xzheng2.cmu.edu.hw3.ViewActivity;

/**
 * Created by zhengqian1 on 6/5/16.
 */

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import xzheng2.cmu.edu.hw3.R;

public class EventListActivity extends ListActivity {

    public static final String ROW_ID = "row_id";
    private ListView eventListView;
//    private CursorAdapter eventAdapter;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get action bar
       setContentView(R.layout.fragment_item_list);

        // Enabling Up / Back navigation
        // actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
//        new GetEventsTask().execute((Object[]) null);
    }

    @Override
    protected void onStop()
    {

        super.onStop();
    }

//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // 1. Add action item (menu xml should be under menu folder )
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.event_list, menu);
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_list, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //   return true;
        //}


        switch (item.getItemId()) {

            case R.id.editEventItem:

                Toast.makeText(this, "To Edit: click the event", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.addEventItem:

//
                return true;
            case R.id.listEventItem:

//
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
