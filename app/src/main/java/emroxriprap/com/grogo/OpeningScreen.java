package emroxriprap.com.grogo;

import android.content.Context;
import android.content.Intent;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import emroxriprap.com.grogo.emroxriprap.com.grogo.db.DbHandler;


public class OpeningScreen extends ActionBarActivity {
    private ListView listviewOfLists;
    private List<GroceryList> groceryList;
    private MyCustomAdapter groceryListAdapter;
    private MyCustomAdapter drawerListAdapter;
    private List<LeftDrawerItem>drawerList;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout drawerWrapperLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        DbHandler db = new DbHandler(this);
//        db.deleteAllLists();
//        addTestData();
//        TESTpopulateShoppingListsItemsTable();
        groceryList = new ArrayList<GroceryList>();
        groceryList = db.getAllLists();

        drawerWrapperLayout = (LinearLayout)findViewById(R.id.ll_drawer_wrapper_layout);
        drawerListView = (ListView)findViewById(R.id.lv_drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setUpDrawerList();
        setupViews();

//      drawerListView.setAdapter(new ArrayAdapter<String>(this,R.layout.list_item_drawer,array));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                                                    R.string.open,  R.string.close ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
//                getActionBar().setTitle("test");
                Toast.makeText(getApplicationContext(),"closed",Toast.LENGTH_SHORT).show();
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle("test");
                Toast.makeText(getApplicationContext(),"opened",Toast.LENGTH_SHORT).show();

            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    private void setupViews(){
        listviewOfLists = (ListView)findViewById(R.id.lv_saved_lists);
        groceryListAdapter = new MyCustomAdapter(this,groceryList);
        listviewOfLists.setAdapter(groceryListAdapter);
        drawerListAdapter = new MyCustomAdapter(this,drawerList);
        drawerListView.setAdapter(drawerListAdapter);

        listviewOfLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Store s = storeList.get(position);
//                Log.d("LOOKIE HERE", "Value is " + s.getStoreName());
//                Intent intent = new Intent(SummaryScreen.this, ListScreen.class);
//                intent.putExtra("id", s.getId());
//                intent.putExtra("name",s.getStoreName());
//                intent.putExtra("address", s.getStoreAddress());
//                startActivity(intent);
                view.setSelected(true);
                GroceryList gl = groceryList.get(position);
                Intent i = new Intent(OpeningScreen.this, ListScreen.class);
                i.putExtra("id", gl.getId());
                i.putExtra("name", gl.getName());
                startActivity(i);

            }
        });
    }
    private void setUpDrawerList() {
        LeftDrawerItem myItems = new LeftDrawerItem("My Items","Add or edit your saved items.  You" +
                " can mark items as favorites and add them to your lists automatically.",
                R.drawable.full_basket_icon);
        LeftDrawerItem myMeals = new LeftDrawerItem("My Meals", "Add or edit your saved meals. " +
                "Adding a meal to your shopping list will automatically add all of the meal's ingredients.",
                R.drawable.meal_icon);
        LeftDrawerItem myLists = new LeftDrawerItem("My Lists", "Add or edit your shopping lists.  " +
                "Lists can consist of items and meals.",
                R.drawable.list_icon);
        LeftDrawerItem settings = new LeftDrawerItem("Settings", "Edit application settings.",
                R.drawable.settings_icon);
        drawerList = new ArrayList<LeftDrawerItem>();
        drawerList.add(myItems);
        drawerList.add(myMeals);
        drawerList.add(myLists);
        drawerList.add(settings);


    }

    private void TESTpopulateShoppingListsItemsTable() {
        DbHandler db = new DbHandler(this);
        db.addEntryToShoppingListsItemsTable(0,2);
        db.addEntryToShoppingListsItemsTable(1,2);
        db.addEntryToShoppingListsItemsTable(2,2);
        db.addEntryToShoppingListsItemsTable(3,1);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    private void addTestData() {
        GroceryList one = new GroceryList("Sunday List",8);
        GroceryList two = new GroceryList("Football Lunch",18);
        GroceryList three = new GroceryList("Parents Anni",4);
        GroceryList four = new GroceryList("Weekly",0);
        boolean success = true;
        DbHandler db = new DbHandler(this);
        success = db.addGroceryList(one);
        success = db.addGroceryList(two);
        success = db.addGroceryList(three);
        success = db.addGroceryList(four);
        if (!success){
            Toast.makeText(this, "Failed to insert into DB", Toast.LENGTH_SHORT).show();
        }
//        groceryList = new ArrayList<GroceryList>();
//        groceryList.add(one);
//        groceryList.add(two);
//        groceryList.add(three);
//        groceryList.add(four);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opening_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (item != null && item.getItemId() == android.R.id.home) {
//            for right side drawer stuff...
//            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
//                drawerLayout.closeDrawer(Gravity.RIGHT);
//            } else {
//                drawerLayout.openDrawer(Gravity.RIGHT);
//            }
//        }

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class MyCustomAdapter extends BaseAdapter {
        private Context myContext;
        private List<?>dataList;

        public MyCustomAdapter(Context context, List<?>list) {
            myContext = context;
            dataList = list;
        }


        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(dataList.get(0) instanceof GroceryList){
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item_white_bg, null);
                }
                TextView nameView = (TextView)convertView.findViewById(R.id.tv_list_item_list_name);
                TextView itemCountView = (TextView)convertView.findViewById(R.id.tv_list_item_count);

                GroceryList gl = groceryList.get(position);
                nameView.setText(gl.getName());
                itemCountView.setText(String.valueOf(gl.getItemCount()));
                return convertView;
            }else if(dataList.get(0) instanceof LeftDrawerItem){
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item_left_drawer, null);
                }
                TextView name = (TextView)convertView.findViewById(R.id.tv_left_drawer_name);
                TextView  desc = (TextView)convertView.findViewById(R.id.tv_left_drawer_description);
                ImageView icon = (ImageView)convertView.findViewById(R.id.iv_left_drawer_icon);

                LeftDrawerItem item = (LeftDrawerItem)dataList.get(position);
                name.setText(item.getName());
                desc.setText(item.getDescription());
                icon.setImageResource(item.getIconResId());
                return convertView;
            }
            return null;
        }
    }
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true);
        setTitle("Replace Me");
        drawerLayout.closeDrawer(drawerWrapperLayout);


    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
