package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by zhengqian1 on 6/5/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class EventDao {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_start, SQLiteHelper.COLUMN_end };



    public EventDao(Context context) {
        dbHelper = new SQLiteHelper (context, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertEvent(String startTime,  String endTime)
    {
        ContentValues newEvent = new ContentValues();
        newEvent.put("start", startTime);
        newEvent.put("end", endTime);
        open();
        database.insert(SQLiteHelper.TABLE_Event, null, newEvent);
        close();
    }


    public void updateEvent( String startTime,  String endTime, long id)
    {
        ContentValues editEvent = new ContentValues();
        editEvent.put("start", startTime);
        editEvent.put("end", endTime);
        open();
        database.update(SQLiteHelper.TABLE_Event, editEvent, "_id=" + id, null);
        close();
    }

    public Cursor getAllEvents()
    {
        return database.query(SQLiteHelper.TABLE_Event, new String[] {"_id", "start","end"}, null, null, null, null, SQLiteHelper.COLUMN_start+" DESC");
    }

    public List<Event> getAllEventLists() {
        List<Event> events = new ArrayList<Event>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_Event,
                allColumns, null, null, null, null, " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event Event = cursorToEvent(cursor);
            events.add(Event);
            cursor.moveToNext();
        } // make sure to close the cursor
        cursor.close();
        return events;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();

        event.setId(cursor.getString(1));
        return event;
    }

    public Cursor getOneEvent(long id)
    {
        return database.query(SQLiteHelper.TABLE_Event, null, "_id=" + id, null, null, null, null);
    }

    public void deleteEvent(long id)
    {
        open();
        database.delete(SQLiteHelper.TABLE_Event, "_id=" + id, null);
        close();
    }
}
