package LaunchFunction.DataConvert;

import LaunchFunction.DataConvert.model.*;
import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LOCSum_LastCommitConverter implements Converter {

    @Override
    public void convert(String csvfilepath, Connection connection, String repoID, boolean Update, List<String> filter) throws Exception {
        CsvReader reader = new CsvReader(csvfilepath, ',', Charset.forName("UTF-8"));
        reader.readHeaders();
        String[] header = reader.getHeaders();

        //prepare dbAttributes
        List<DBAttribute> dbAttributes = new ArrayList<>();
        dbAttributes.add(new DBAttribute<>("name", DBAttribute.STRING_CONTENT, true));
        dbAttributes.add(new DBAttribute<>("commits", DBAttribute.STRING_CONTENT, true));
        dbAttributes.add(new DBAttribute<>("lastcommittime", DBAttribute.STRING_CONTENT, true));


        //prepare dbTuples
        List<DBTuple> dbTuples = new ArrayList<>();
        for (int i=2; i<header.length; i++){
            dbTuples.add(new DBTuple(3).add(new DBValue<>(header[i])));
        }

        String[] last_time = new String[header.length];
        int[] LOC_sum = new int[header.length];

        while(reader.readRecord()){
            String[] raw_tuple = reader.getValues();

            for (int i=2; i<raw_tuple.length; i++){
                if(Integer.parseInt(raw_tuple[i]) != 0){
                    last_time[i] = raw_tuple[0];
                }
                LOC_sum[i] += Integer.parseInt(raw_tuple[i]);
            }
        }

        for (int i=2; i<header.length; i++){
            dbTuples.get(i-2).add(new DBValue<>(LOC_sum[i])).add(new DBValue<>(last_time[i]));
        }

        //prepare dbTable Name
        String dbTableName = "LOCSumLastCommit" + repoID;
        DBTable dbTable = new DBTable(dbTableName, dbTuples, dbAttributes);

        dbTable.insertToSQL(connection, Update);
    }

}
