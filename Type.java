import java.util.ArrayList;
import java.util.List;

public class Type {
    private String type;
    private List<Item> items=new ArrayList<Item>();
    List<Property> defaultProperties=new ArrayList<Property>();

    public String toString(){
        return type;
    }
    public void addItems(Item item){
        items.add(item);
    }
    public void addDefaultProperties(Property property) {
        defaultProperties.add(property);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Property> getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(List<Property> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public Type(String type) {
        this.type = type;
    }
}
