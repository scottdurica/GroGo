package emroxriprap.com.grogo.emroxriprap.com.grogo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import emroxriprap.com.grogo.models.GroceryList;
import emroxriprap.com.grogo.models.Item;

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
    public static final String COLUMN_LIST_ITEMCOUNT= "list_item_count";

    /*shopping_lists_items fields*/
    public static final String COLUMN_ID = "id";
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
        String CREATE_SHOPPING_LIST_TABLE = "CREATE TABLE " + TABLE_SHOPPING_LISTS + "(" + COLUMN_LIST_ID +
                " INTEGER PRIMARY KEY," + COLUMN_LIST_NAME + " VARCHAR," + COLUMN_LIST_ITEMCOUNT +
                " INTEGER" + ")";
        String CREATE_SHOPPING_LISTS_ITEMS_TABLE = "CREATE TABLE " + TABLE_SHOPPING_LISTS_ITEMS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_ITEM_ID + " INTEGER," +
                COLUMN_LIST_ID + " INTEGER" +
                ")";
Log.e("String is ", CREATE_SHOPPING_LISTS_ITEMS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_STORES_TABLE);
        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
        db.execSQL(CREATE_SHOPPING_LISTS_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LISTS_ITEMS);
        onCreate(db);
    }

    public boolean addGroceryList(GroceryList list){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIST_NAME, list.getName());
        values.put(COLUMN_LIST_ITEMCOUNT,list.getItemCount());

        SQLiteDatabase db = this.getWritableDatabase();
        long finished = db.insert(TABLE_SHOPPING_LISTS, null, values);
        db.close();
        if (finished != -1){
            return true;
        }
        return false;
    }

    public List<GroceryList> getAllLists(){
        List<GroceryList>list = new ArrayList<GroceryList>();
        String query = "Select * from " + TABLE_SHOPPING_LISTS + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                GroceryList gl = new GroceryList();
                gl.setId((Integer.parseInt(cursor.getString(0))));
                gl.setName(cursor.getString(1));
//                gl.setItemCount(Integer.parseInt(cursor.getString(2)));
                gl.setItemCount(getListItemCount(gl.getId()));
                list.add(gl);
            }while(cursor.moveToNext());
        }
        return list;
    }
    //helper method for getAllLists- figures the number of items in each list
    private int getListItemCount(int listId){
        String query = "Select * FROM " + TABLE_SHOPPING_LISTS_ITEMS +
                " WHERE " + COLUMN_LIST_ID + "=" + listId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count =0;
        if (cursor.moveToFirst()){
            do {
                count ++;
            }while(cursor.moveToNext());
        }
        return count;
    }
    public void deleteAllLists(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SHOPPING_LISTS);
    }

    public boolean addItem(Item item){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, item.getName());
        SQLiteDatabase db = this.getWritableDatabase();
        long finished = db.insert(TABLE_ITEMS,null,values);
        db.close();
        if (finished != -1){
            Log.d("****************************************", "SAVED");
            return true;
        }
        return false;
    }
    public int getIdOfItem(Item item){
        String query = "Select " + COLUMN_ITEM_ID + " from " + TABLE_ITEMS +
                " where " + COLUMN_ITEM_NAME + "=" + "'" + item.getName() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int value = -1;
        if (cursor.moveToFirst()){
            do {
                value = cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        return value;
    }

    public List<Item>getItemsForList(int listId){
        List<Item>list = new ArrayList<Item>();
        String query = "Select " + TABLE_ITEMS + "." + COLUMN_ITEM_ID + ","
                + TABLE_ITEMS + "." + COLUMN_ITEM_NAME +
                " FROM " + TABLE_ITEMS +
                " INNER JOIN " + TABLE_SHOPPING_LISTS_ITEMS +
                " ON " + TABLE_ITEMS + "." + COLUMN_ITEM_ID + "=" + TABLE_SHOPPING_LISTS_ITEMS
                + "." + COLUMN_ITEM_ID + " WHERE " + COLUMN_LIST_ID + "=" + listId ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                Item item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setName(cursor.getString(1));
                list.add(item);
//                Log.e("NAME: ",cursor.getString(1));
//                Log.e("ID: ",String.valueOf(cursor.getInt(0)));
            }while(cursor.moveToNext());
        }
        return list;
    }

    public boolean addEntryToShoppingListsItemsTable(int itemId, int listId){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID, itemId);
        values.put(COLUMN_LIST_ID, listId);
        SQLiteDatabase db = this.getWritableDatabase();
        long finished = db.insert(TABLE_SHOPPING_LISTS_ITEMS,null,values);
        db.close();
        if (finished != -1){
            Log.d("****************************************", " LISTITEMS SAVED");
            return true;
        }
        return false;

    }
    public void deleteItemFromList(int itemId, int listId){
        String query = "DELETE " + " from " + TABLE_SHOPPING_LISTS_ITEMS +
                " where " + COLUMN_ITEM_ID + "=" + itemId + " AND " + COLUMN_LIST_ID
                + "=" + listId;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
    public boolean doesEntryExistInList(int itemId, int listId){
        String query = "Select * " + " from " + TABLE_SHOPPING_LISTS_ITEMS +
                " where " + COLUMN_ITEM_ID + "=" + itemId + " AND " + COLUMN_LIST_ID
                 + "=" + listId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }
}
