package emroxriprap.com.grogo.manipulationscreens;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import emroxriprap.com.grogo.R;
import emroxriprap.com.grogo.emroxriprap.com.grogo.db.DbHandler;
import emroxriprap.com.grogo.models.Item;


public class MealsScreenUtility extends ActionBarActivity {

    private RecyclerView recyclerView;
    private DbHandler db;
    private List<Item> itemList;
    private MyCustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_screen);

        recyclerView = (RecyclerView)findViewById(R.id.rv_item_card_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        db = new DbHandler(this);
        itemList = db.getAllItems();
        adapter = new MyCustomAdapter(itemList);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meals_screen, menu);
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
    public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ItemViewHolder> {

        private List<Item> dataList;

        public MyCustomAdapter(List<Item> contactList) {
            this.dataList = contactList;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public void onBindViewHolder(ItemViewHolder contactViewHolder, int i) {
            Item item = dataList.get(i);
            contactViewHolder.name.setText(item.getName());

        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_layout_item, viewGroup, false);

            return new ItemViewHolder(itemView);
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView name;

            public ItemViewHolder(View view) {
                super(view);
                name = (TextView)view.findViewById(R.id.tv_card_item_name);

            }
        }
    }
}
