package emroxriprap.com.grogo.models;

/**
 * Created by Scott Durica on 1/19/2015.
 */
public class LeftDrawerItem {
    private String name;
    private String description;
    private int iconResId;

    public LeftDrawerItem(){

    }
    public LeftDrawerItem(String name, String description, int iconResId){
        this.name = name;
        this.description = description;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
