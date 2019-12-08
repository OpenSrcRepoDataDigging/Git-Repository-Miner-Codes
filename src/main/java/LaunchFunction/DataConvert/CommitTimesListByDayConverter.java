package LaunchFunction.DataConvert;

import LaunchFunction.DataConvert.model.*;
import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CommitTimesListByDayConverter implements Converter{

    @Override
    public void convert(String csvfilepath, Connection connection, String repoID, boolean Update, List<String> filter) throws Exception {
        CsvReader reader = new CsvReader(csvfilepath, ',', Charset.forName("UTF-8"));
        reader.readHeaders();
        String[] header = reader.getHeaders();

        int[] flags = new int[header.length];
        flags[0] = 1;
        for (int i=1; i<flags.length; i++){
            if (filter.contains(header[i])){
                flags[i] = 1;
            }
            else{
                flags[i] = 0;
            }
        }

        for (int i=0; i<header.length; i++){
            for (int j=i+1; j<header.length; j++){
                if (header[i].toUpperCase().equals(header[j].toUpperCase())){
                    header[j] += "_dt";
                }
            }
        }

        //prepare dbAttributes
        List<DBAttribute> dbAttributes = new ArrayList<>();
        dbAttributes.add(new DBAttribute<>(header[0], DBAttribute.STRING_CONTENT, true));
        dbAttributes.add(new DBAttribute<>("total", DBAttribute.STRING_CONTENT, true));
        for (int i=2; i<header.length; i++){
            if (flags[i]==1)
                dbAttributes.add(new DBAttribute<>(header[i], DBAttribute.LONG_CONTENT, true));
        }

        //prepare dbTuples
        List<DBTuple> dbTuples = new ArrayList<>();
        while(reader.readRecord()){
            String[] raw_tuple = reader.getValues();
            DBTuple dbt = new DBTuple(raw_tuple.length);
            dbt.add(new DBValue<>(raw_tuple[0]));
            for (int i=1; i<raw_tuple.length; i++){
                if (flags[i]==1)
                    dbt.add(new DBValue<>(Integer.parseInt(raw_tuple[i])));
            }
            dbTuples.add(dbt);
        }

        //prepare dbTable Name
        String dbTableName = "CommitTimesListByDay" + repoID;
        DBTable dbTable = new DBTable(dbTableName, dbTuples, dbAttributes);

        dbTable.insertToSQL(connection, Update);
    }

}
