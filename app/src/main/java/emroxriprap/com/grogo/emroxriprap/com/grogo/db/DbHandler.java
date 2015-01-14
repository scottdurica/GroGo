package emroxriprap.com.grogo.emroxriprap.com.grogo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by administrator on 1/14/15.
 */
public class DbHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "grogo.db";

    /* table names */
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_STORES = "stores";
    private static final String TABLE_STORES_ITEMS = "stores_items";
    private static final String TABLE_SHOPPING_LISTS = "shopping_lists";
    private static final String TABLE_SHOPPING_LISTS_ITEMS = "shopping_lists_items";


    /* items fields */
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_NAME = "item_name";

    /* stores fields */
    public static final String COLUMN_STORE_ID = "store_id";
    public static final String COLUMN_STORE_NAME = "store_name";
    public static final String COLUMN_STORE_ADDRESS = "store_address";

    /* stores_items fields */
    public static final String COLUMN_STORES_ITEMS_ID= "stores_items_id";//????????
    public static final String COLUMN_STORES_ITEMS_AISLE= "aisle";
    //ADDITIONAL FIELDS IN THIS TABLE: STORE_ID, ITEM_ID

    /*shopping_lists fields*/
    public static final String COLUMN_LIST_ID= "list_id";
    public static final String COLUMN_LIST_NAME= "list_name";

    /*shopping_lists_items fields*/
    //ADDITIONAL FIELDS IN THIS TABLE: LIST_ID, ITEM_ID


    /* Constructors */
    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);

    }
    public DbHandler(Context context,String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "(" + COLUMN_ITEM_ID +
                    " INTEGER PRIMARY KEY," + COLUMN_ITEM_NAME + " VARCHAR" + ")";

        String CREATE_STORES_TABLE = "CREATE TABLE " + TABLE_STORES + "(" + COLUMN_STORE_ID +
                    " INTEGER PRIMARY KEY," + COLUMN_STORE_NAME + " VARCHAR," +
                     COLUMN_STORE_ADDRESS + " VARCHAR" +  ")";


                db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }






}
