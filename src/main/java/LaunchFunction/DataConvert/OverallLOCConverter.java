package LaunchFunction.DataConvert;

import LaunchFunction.DataConvert.model.*;
import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.*;

public class OverallLOCConverter implements Converter{

    private Queue<Pair> gen_filter = null;
    public static int FILTER_NUMBER = 50;
    //Map<String, Integer> gen_filter = null;

    @Override
    public void convert(String csvfilepath, Connection connection, String repoID, boolean Update, List<String> filter) throws Exception {

        this.gen_filter = new PriorityQueue<>();

        if (filter != null){
            throw new NullPointerException("filter must be null in OverallLOCConverter!");
        }

        CsvReader reader = new CsvReader(csvfilepath, ',', Charset.forName("UTF-8"));
        reader.readHeaders();
        String[] header = reader.getHeaders();

        List<DBAttribute> dbAttributeList = new ArrayList<>();
        dbAttributeList.add(new DBAttribute<>(header[0], DBAttribute.STRING_CONTENT, true));
        for (int i=1; i<header.length; i++){
            dbAttributeList.add(new DBAttribute<>(header[i], DBAttribute.LONG_CONTENT, true));
        }


        List<DBTuple> dbTuples = new ArrayList<>();
        while (reader.readRecord()){
            String name;
            int loc = 0;
            String[] raw_tuple = reader.getValues();
            DBTuple dbt = new DBTuple(raw_tuple.length);
            dbt.add(new DBValue<>(raw_tuple[0]));
            name = raw_tuple[0];
            for (int i=1; i<raw_tuple.length; i++){
                dbt.add(new DBValue<>(Integer.parseInt(raw_tuple[i])));
                loc += Integer.parseInt(raw_tuple[i]);
            }
            dbTuples.add(dbt);
            gen_filter.add(new Pair(name, loc));
            if(gen_filter.size() > 20){
                gen_filter.poll();
            }

        }

        String dbTableName = "OverallLOCList" + repoID;

        DBTable dbTable = new DBTable(dbTableName, dbTuples, dbAttributeList);

        dbTable.insertToSQL(connection, Update);

    }

    public List<String> getGen_filter() {
        Object[] arr = gen_filter.toArray();

        List<String> result = new ArrayList<>();

        for (Object o : arr){
            result.add(((Pair)o).name);
        }

        return result;
    }

    public static class Pair implements Comparable<Pair>{
        public String name;
        public int LOC;

        public Pair(String name, int LOC){
            this.name = name;
            this.LOC = LOC;
        }

        @Override
        public int compareTo(Pair o) {
            return LOC-o.LOC;
        }
    }
}
