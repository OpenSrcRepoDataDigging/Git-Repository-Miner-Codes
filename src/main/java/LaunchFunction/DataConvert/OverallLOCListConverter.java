package LaunchFunction.DataConvert;

import LaunchFunction.DataConvert.model.DBAttribute;
import LaunchFunction.DataConvert.model.DBTable;
import LaunchFunction.DataConvert.model.DBTuple;
import LaunchFunction.DataConvert.model.DBValue;
import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class OverallLOCListConverter implements Converter {

    @Override
    public void convert(String csvfilepath, Connection connection, String repoID, boolean Update) throws Exception {
        CsvReader reader = new CsvReader(csvfilepath, ',', Charset.forName("UTF-8"));
        reader.readHeaders();
        String[] header = reader.getHeaders();

        //prepare dbAttributes
        List<DBAttribute> dbAttributes = new ArrayList<>();
        dbAttributes.add(new DBAttribute<>(header[0], DBAttribute.STRING_CONTENT, true));
        for (int i=1; i<header.length; i++){
            dbAttributes.add(new DBAttribute<>(header[i], DBAttribute.LONG_CONTENT, true));
        }

        //prepare dbTuples
        List<DBTuple> dbTuples = new ArrayList<>();
        while(reader.readRecord()){
            String[] raw_tuple = reader.getValues();
            DBTuple dbt = new DBTuple(raw_tuple.length);
            dbt.add(new DBValue<>(raw_tuple[0]));
            for (int i=1; i<raw_tuple.length; i++){
                dbt.add(new DBValue<>(Integer.parseInt(raw_tuple[i])));
            }
            dbTuples.add(dbt);
        }

        //prepare dbTable Name
        String dbTableName = "OverallLOCList" + repoID;
        DBTable dbTable = new DBTable(dbTableName, dbTuples, dbAttributes);

        dbTable.insertToSQL(connection, Update);
    }

}
