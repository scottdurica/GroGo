package emroxriprap.com.grogo.models;

/**
 * Created by Scott Durica on 1/23/2015.
 */
public class GroceryList {

    private String name;
    private int id;
    private int itemCount;

    public GroceryList(){

    }
    public GroceryList(String name, int id){
        this.name = name;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int count){
        this.itemCount = count;
    }
}
