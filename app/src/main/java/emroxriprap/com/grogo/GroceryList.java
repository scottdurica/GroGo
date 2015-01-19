package emroxriprap.com.grogo;

import java.util.List;

/**
 * Created by Scott Durica on 1/11/2015.
 */
public class GroceryList {
    private int id;
    private String name;
    private int itemCount;


    public GroceryList() {

    }

    public GroceryList(String name, int itemCount) {
        this.name = name;
        this.itemCount = itemCount;

    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
