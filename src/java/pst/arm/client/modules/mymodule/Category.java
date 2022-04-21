package pst.arm.client.modules.mymodule;

import com.extjs.gxt.ui.client.data.BeanModelTag;

import java.util.ArrayList;
import java.util.List;

public class Category implements BeanModelTag {
    private String name;
    private List<Category> child = new ArrayList<Category>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getChild() {
        return child;
    }

    public void setChild(List<Category> child) {
        this.child = child;
    }

}
