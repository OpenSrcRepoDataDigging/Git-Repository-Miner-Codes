package LaunchFunction.DataConvert.model;

import exception.exceptions.DBAttributeNotSupportException;
import exception.exceptions.DBTupleReachMaxSizeException;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBTuple {
    private List<DBValue> value_list = new ArrayList<DBValue>();
    private Integer fixed_size;

    public DBTuple (Integer fixed_size){
        this.fixed_size = fixed_size;
    }

    public void addDBValue(DBValue dbValue) throws DBTupleReachMaxSizeException {
        if (value_list.size() >= fixed_size){
            // Log:
            throw new DBTupleReachMaxSizeException();
        }
        value_list.add(dbValue);
    }

    public void prepareSQLStatement(PreparedStatement ptmt) {
        for (int i=0; i<value_list.size(); i++){
            DBValue dbv = value_list.get(i);
            try {
                if (dbv.getDbvalue() instanceof String) {
                    ptmt.setString(i + 1, (String) dbv.getDbvalue());
                }
                else if (dbv.getDbvalue() instanceof Integer) {
                    ptmt.setLong(i + 1, (Integer)dbv.getDbvalue());
                }
                else if (dbv.getDbvalue() instanceof Double){
                    ptmt.setDouble(i + 1, (Double)dbv.getDbvalue());
                }
                else {
                    throw new DBAttributeNotSupportException();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

class DBValue<T> {
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
