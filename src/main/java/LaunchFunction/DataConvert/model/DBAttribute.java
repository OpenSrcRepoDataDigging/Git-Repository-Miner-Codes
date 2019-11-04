package LaunchFunction.DataConvert.model;

import exception.exceptions.DBAttributeNotSupportException;

import java.util.ArrayList;
import java.util.List;

public class DBAttribute<T> {
    private String attr_name;
    private T attr_content;
    private Boolean not_null;

    public DBAttribute(String attr_name, T attr_content, Boolean not_null){
        this.attr_name = attr_name;
        this.attr_content = attr_content;
        this.not_null = not_null;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public T getAttr_content() {
        return attr_content;
    }

    public void setAttr_content(T attr_content) {
        this.attr_content = attr_content;
    }

    public String getSQLTypeString() throws DBAttributeNotSupportException{

        if (attr_content instanceof Integer){
            return "LONG";
        }
        else if(attr_content instanceof String){
            return "VARCHAR(255)";
        }
        else if(attr_content instanceof Double){
            return "DOUBLE";
        }
        // more types...
        else{
            throw new DBAttributeNotSupportException();
        }

    }

    public String getSQLNotNullString(){
        if (not_null){
            return "NOT NULL";
        }
        return "";
    }

    /*
    public static void main(String[] args){
        List<DBAttribute> list = new ArrayList<DBAttribute>();
        list.add(new DBAttribute<String>("name", "Jack"));
        list.add(new DBAttribute<Integer>("type", 15));
    }
    */
}
