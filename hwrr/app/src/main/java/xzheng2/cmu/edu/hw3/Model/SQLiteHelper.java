package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by zhengqian1 on 6/5/16.
 */
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//
//public class SQLiteHelper extends SQLiteOpenHelper {
//
//
//    public static final String TABLE_Event = "eventlist";
//    private static final String TEXT_TYPE = " TEXT";
//    private static final String COMMA_SEP = ",";
//
//    public static final String DATABASE_NAME = "Event";
//    public static final int DATABASE_VERSION = 1;
//
//    public static final String COLUMN_ID = "_id";
//
//    public static final String COLUMN_startTime = "startTime";
//    public static final String COLUMN_endTime = "endTime";
//
//
//
//    private static final String DATABASE_CREATE1 = "CREATE TABLE " +TABLE_Event+
//            "(_id integer primary key autoincrement," + COLUMN_startTime+ TEXT_TYPE+COMMA_SEP+
//            COLUMN_endTime+TEXT_TYPE+");";
////            "startTime TEXT, " +
////            "endTime TEXT);";
////    private static final String DATABASE_CREATE = "CREATE TABLE " +TABLE_Event+
////            "(_id integer primary key autoincrement," +
////            "name TEXT, " +
////            "dateTime TEXT, location TEXT);";
//
//    /**
//     * @param context
//     * @param name
//     * @param factory
//     * @param version
//     */
//    public SQLiteHelper(Context context, String name, CursorFactory factory,
//                        int version) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        // TODO Auto-generated constructor stub
//    }
//
//    /* (non-Javadoc)
//     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
//     */
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // TODO Auto-generated method stub
//        db.execSQL(DATABASE_CREATE1);
//
//
//    }
//
//    /* (non-Javadoc)
//     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
//     */
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // TODO Auto-generated method stub
//
//        Log.w(SQLiteHelper.class.getName(),
//                "Upgrading database from version " + oldVersion + " to "
//                        + newVersion + ", which will destroy all old data"
//        );
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Event);
//
//
//        onCreate(db);
//    }
//
//}
/**
 *
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author saeyoungjeong
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    public static final String TABLE_Event = "eventlist";

    public static final String DATABASE_NAME = "Event";
    public static final int DATABASE_VERSION = 2;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_start = "start";
    public static final String COLUMN_end = "end";
//    public static final String COLUMN_location = "_location";

    //	private static final String DATABASE_CREATE = "CREATE TABLE " +TABLE_Event+
//			"(_id integer primary key autoincrement," +
//			"name TEXT, " +
//			"dateTime TEXT, location TEXT);";
    private static final String DATABASE_CREATE = "CREATE TABLE " +TABLE_Event+
            "(_id integer primary key autoincrement," +
            "start TIMESTAMP, " +
            "end TIMESTAMP);";
    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DATABASE_CREATE);

    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Event);

        onCreate(db);
    }

}
