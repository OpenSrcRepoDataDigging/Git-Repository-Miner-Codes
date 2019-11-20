package DataConvert;

import LaunchFunction.DataConvert.model.DBAttribute;
import LaunchFunction.DataConvert.model.DBTable;
import LaunchFunction.DataConvert.model.DBTuple;
import LaunchFunction.DataConvert.model.DBValue;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class DBTableTest {

    @Test
    public void TestDBTable(){

        List<DBAttribute> dbAttributes = new ArrayList<>();
        dbAttributes.add(new DBAttribute<>("name", DBAttribute.STRING_CONTENT, true));
        dbAttributes.add(new DBAttribute<>("age", DBAttribute.LONG_CONTENT, true));
        dbAttributes.add(new DBAttribute<>("department", DBAttribute.STRING_CONTENT, true));
        dbAttributes.add(new DBAttribute<>("gender", DBAttribute.STRING_CONTENT, true));

        List<DBTuple> dbTuples = new ArrayList<>();

        DBTuple dbt1 = new DBTuple(4);
        try {
            dbt1.add(new DBValue<>("a")).add(new DBValue<Integer>(1)).add(new DBValue<>("cs")).add(new DBValue<>("male"));
        } catch (Exception e){
            e.printStackTrace();
        }

        DBTuple dbt2 = new DBTuple(4);
        try {
            dbt2.add(new DBValue<>("b")).add(new DBValue<>(2)).add(new DBValue<>("se")).add(new DBValue<>("female"));
        } catch (Exception e){
            e.printStackTrace();
        }

        dbTuples.add(dbt1);
        dbTuples.add(dbt2);

        String dbName = "Salary";

        DBTable dbTable = new DBTable(dbName, dbTuples, dbAttributes);

        try {
            Class.forName("org.sqlite.JDBC");
            //String pathname = "/home/"+System.getProperty("user.name")+"/test.db";
            String pathname = "/test.db";
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathname);

            dbTable.insertToSQL(connection, false);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void TestSplit(){
        String str = "localpath/0/";
        System.out.println(str.split("/")[str.split("/").length-1]);
    }

}
