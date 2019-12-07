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

public class ContributorNetworkConverter implements Converter {

    private int getCommonFileNumber(String[] lst1, String[] lst2){
        int result = 0;

        if (lst1[0].equals(lst2[0]))
            return result;

        for (int i=1; i< lst1.length; i++){
            if (Integer.parseInt(lst1[i]) != 0 && Integer.parseInt(lst2[i]) != 0){
                result++;
            }
        }

        return result;
    }

    @Override
    public void convert(String csvfilepath, Connection connection, String repoID, boolean Update) throws Exception {
        CsvReader reader = new CsvReader(csvfilepath, ',', Charset.forName("UTF-8"));
        reader.readHeaders();
        String[] header = reader.getHeaders();
        for (int i=0; i<header.length; i++){
            for (int j=i+1; j<header.length; j++){
                if (header[i].toUpperCase().equals(header[j].toUpperCase())){
                    header[j] += "_dt";
                }
            }
        }

        List<DBAttribute> dbAttributes = new ArrayList<>();
        List<DBTuple> dbTuples = new ArrayList<>();

        List<String[]> raw_tuples = new ArrayList<>();
        while(reader.readRecord()){
            String[] raw_tuple = reader.getValues();
            raw_tuples.add(raw_tuple);
        }

        dbAttributes.add(new DBAttribute<>("name", DBAttribute.STRING_CONTENT, true));

        for (String[] t1 : raw_tuples){
            dbAttributes.add(new DBAttribute<>(t1[0], DBAttribute.LONG_CONTENT, true));
            DBTuple dbt = new DBTuple(raw_tuples.size() + 1);
            dbt.add(new DBValue<>(t1[0]));
            for (String[] t2 : raw_tuples){
                int result = getCommonFileNumber(t1, t2);
                dbt.add(new DBValue<>(result));
            }
            dbTuples.add(dbt);
        }

        String dbTableName = "ContributorNetworkMatrix" + repoID;
        DBTable dbTable = new DBTable(dbTableName, dbTuples, dbAttributes);

        dbTable.insertToSQL(connection, Update);
    }
}
