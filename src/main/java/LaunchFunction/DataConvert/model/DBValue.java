package LaunchFunction.DataConvert.model;

public class DBValue<T> {
    private T dbvalue;

    public DBValue(T dbvalue){
        this.dbvalue = dbvalue;
    }

    public T getDbvalue() {
        return dbvalue;
    }

    public void setDbvalue(T dbvalue) {
        this.dbvalue = dbvalue;
    }
}
