package emroxriprap.com.grogo;

import android.content.Context;
import android.content.Intent;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static emroxriprap.com.grogo.R.drawable.*;


public class OpeningScreen extends ActionBarActivity {
    private ListView listviewOfLists;
    private List<GroceryList> groceryList;
    private MyCustomAdapter adapter;

private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    String[] array = new String[]{"one", "two", "three","blah", "be", "blah",
            "one", "two", "three","blah", "be", "blah",
            "one", "two", "three","blah", "be", "blah",
            "one", "two", "three","blah", "be", "blah",};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        addTestData();

        setupViews();

        drawerListView = (ListView)findViewById(R.id.lv_drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        drawerListView.setAdapter(new ArrayAdapter<String>(this,R.layout.list_item_drawer,array));
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.open,  /* "open drawer" description */
                R.string.close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
//                getActionBar().setTitle("test");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle("test");
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addTestData() {
        GroceryList one = new GroceryList("Sunday List",8);
        GroceryList two = new GroceryList("Football Day",18);
        GroceryList three = new GroceryList("Parents Over",4);
        GroceryList four = new GroceryList("Bi-Weekly",0);
        groceryList = new ArrayList<GroceryList>();
        groceryList.add(one);
        groceryList.add(two);
        groceryList.add(three);
        groceryList.add(four);

    }

    private void setupViews(){
        listviewOfLists = (ListView)findViewById(R.id.lv_saved_lists);
        adapter = new MyCustomAdapter(this,groceryList);
        listviewOfLists.setAdapter(adapter);
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
            }
        });
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
            }
            return null;
        }
    }
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        drawerListView.setItemChecked(position, true);
        setTitle(array[position]);
        drawerLayout.closeDrawer(drawerListView);
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
//            selectItem(position);
        }
    }
}
