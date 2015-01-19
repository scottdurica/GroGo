package emroxriprap.com.grogo;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import emroxriprap.com.grogo.emroxriprap.com.grogo.db.DbHandler;


public class ListScreen extends ActionBarActivity  {

    private int listId;
    private ListView listView;
    private EditText addField;
    private List<Item> itemList;
    private MyCustomAdapter adapter;

    private GestureDetectorCompat gestureDetector;
    View.OnTouchListener gestureListener;
private int pressedPos = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = getIntent().getStringExtra("name");
        setTitle(name);
        setContentView(R.layout.activity_list_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listId = getIntent().getIntExtra("id",0);

        setupViews();

    }

    private void setupViews() {
        final DbHandler db = new DbHandler(ListScreen.this);

        addField = (EditText)findViewById(R.id.et_add_item);
        itemList = new ArrayList<Item>();
        itemList = db.getItemsForList(listId);
        listView = (ListView)findViewById(R.id.lv_list_items);
        adapter = new MyCustomAdapter(this,R.layout.list_item_item_list,itemList);

        listView.setAdapter(adapter);
        //*
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = listView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.listscreen_cab,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Item selecteditem = (Item)adapter.getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                adapter.remove(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("LONGPRESSED was: ", itemList.get(position).toString());
////                view.findViewById(R.id.ib_delete_item).setVisibility(View.VISIBLE);
//                pressedPos = position;
////                view.setSelected(true);
//                adapter.notifyDataSetChanged();
//                return true;
//            }
//        });
//listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        pressedPos = -1;
//        adapter.notifyDataSetChanged();
//        Log.d("CLICKED was: ", itemList.get(position).toString());
//
//    }
//});

        addField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Item item = new Item(addField.getText().toString());
                    //make sure item doesn't already exist...
                    int exists = db.getIdOfItem(item);
                    if (exists == -1){
                        db.addItem(item);
                        Log.e("ITEM ADDED"," SERIOUSLY!!!");
                    }
                    int id = db.getIdOfItem(item);
                    Log.e("id of item is: ",String.valueOf(id));
                    /*make sure entry isn't already in DB, as this would cause duplicate item
                    names in the listview*/
                    boolean alreadyInList = db.doesEntryExistInList(id,listId);
                    if (!alreadyInList){
                        db.addEntryToShoppingListsItemsTable(id,listId);
                        itemList.add(item);
                        adapter.notifyDataSetChanged();
                    }

                    addField.setText("");
                }
                return false;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class MyCustomAdapter extends ArrayAdapter<Item> {
        // Declare Variables
        Context context;
        LayoutInflater inflater;
        List<Item> dataList;
        private SparseBooleanArray mSelectedItemsIds;

        public MyCustomAdapter(Context context, int resourceId,
                               List<Item> list) {
            super(context, resourceId, list);
            mSelectedItemsIds = new SparseBooleanArray();
            this.context = context;
            this.dataList = list;
            inflater = LayoutInflater.from(context);
        }

        private class ViewHolder {
            TextView itemName;

        }

        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.list_item_item_list, null);
                // Locate the TextViews in listview_item.xml
                holder.itemName = (TextView) view.findViewById(R.id.tv_item_name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            // Capture position and set to the TextViews
            Log.d("Data in list: ", "" + dataList.get(position).getName());
            Log.d("Position is: ", "" + position);
            holder.itemName.setText(dataList.get(position).getName());

            return view;
        }
        @Override
        public int getCount() {
            return dataList.size();
        }
        @Override
        public void remove(Item object) {
            dataList.remove(object);
            notifyDataSetChanged();
        }

        public List<Item> getAllItems() {
            return dataList;
        }

        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }

        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        public void selectView(int position, boolean value) {
            if (value)
                mSelectedItemsIds.put(position, value);
            else
                mSelectedItemsIds.delete(position);
            notifyDataSetChanged();
        }

        public int getSelectedCount() {
            return mSelectedItemsIds.size();
        }

        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }
    }
//    private class MyCustomAdapter extends BaseAdapter{
//
//        private Context myContext;
//        private List<?>dataList;
//
//        public MyCustomAdapter(Context context, List<?>list) {
//            myContext = context;
//            dataList = list;
//        }
//        @Override
//        public int getCount() {
//            return dataList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            if(dataList.get(0) instanceof Item){
//                if (convertView == null) {
//                    LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    convertView = inflater.inflate(R.layout.list_item_item_list, null);
//                }
//                ImageButton button = (ImageButton)convertView.findViewById(R.id.ib_delete_item);
//
//                TextView nameView = (TextView)convertView.findViewById(R.id.tv_item_name);
//                TextView itemCountView = (TextView)convertView.findViewById(R.id.tv_list_item_count);
//                if (pressedPos != position){
//                    button.setVisibility(View.INVISIBLE);
//                }else{
//                    button.setVisibility(View.VISIBLE);
//                    button.setClickable(true);
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //delete item from shopping_list_items table.Redraw list
//                            Item item =itemList.get(position);
////                            Log.d("NAME IF DELETING ITEM IS ",item.getName());
//                            DbHandler db = new DbHandler(ListScreen.this);
//                            int itemId=item.getId();
//                            db.deleteItemFromList(itemId,listId);
//                            itemList.remove(position);
//                            pressedPos = -1;
//                            adapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//                Item item =(Item)dataList.get(position);
//                nameView.setText(item.getName());
////                itemCountView.setText(String.valueOf(gl.getItemCount()));
//                return convertView;
//            }
//            return null;
//        }
//    }
}
