package LaunchFunction.DataConvert;

import LaunchFunction.DataConvert.model.*;
import com.csvreader.CsvReader;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileContributorMatrixConverter implements Converter {

    // original data should also be a field of Converter.
    // This kinds of structure can be optimized by stream process.
    // DBTable target_table = new DBTable();

    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String localdb ="/home/miragelyu/test.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + localdb);
        } catch (Exception e){
            e.printStackTrace();
        }

        String str = "wangsen\n" +
                "xwj0152\n" +
                "WilliamLyu\n" +
                "shiyujie23242004\n" +
                "qxyyqx\n" +
                "niruntao\n" +
                "HuangWenxuan\n" +
                "xyh\n" +
                "Abigiris\n" +
                "lidayou\n" +
                "Mars Chen\n" +
                "Maplewings\n" +
                "Huanyu Wang\n" +
                "zmy\n" +
                "LintianShi\n" +
                "30558264\n" +
                "AlumLuther\n" +
                "Oceanzzzz\n" +
                "wmFrank\n" +
                "smallchen\n" +
                "weiyuang\n" +
                "BYGX-wcr\n" +
                "zfq005\n" +
                "BeiMaGang\n" +
                "Nak\n" +
                "161220127\n" +
                "njucszxy\n" +
                "lengjiayi\n" +
                "turncoat54\n" +
                "161220004\n" +
                "Draculair\n" +
                "qunqun\n" +
                "NJUTto\n" +
                "YmHAHAHa\n" +
                "Konnase Lee\n" +
                "Wh-night\n" +
                "MaBai\n" +
                "LeoDarcy\n" +
                "cockroach20168\n" +
                "161220012\n" +
                "nap-ghj\n" +
                "mkx22\n" +
                "TianChenjiang\n" +
                "KSDeng\n" +
                "xulijun\n" +
                "Young\n" +
                "MirageLyu\n" +
                "courseralxy\n" +
                "czhnju161220026\n" +
                "wga";
        String[] filter = str.split("\n");

        try{
            new FileContributorMatrixConverter().convert("/home/miragelyu/.gitminer/5/csv/fcm.csv", connection, "3", false, Arrays.asList(filter));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

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


        for (int i=1; i<header.length; i++){
            for (int j=i+1; j<header.length; j++){
                if (header[i].toUpperCase().equals(header[j].toUpperCase())){
                    header[j] += "_dt";
                }
            }
        }

        // prepare dbAttributes
        List<DBAttribute> dbAttributes = new ArrayList<>();
        dbAttributes.add(new DBAttribute<>(header[0], DBAttribute.STRING_CONTENT, true));
        for (int i=1; i<header.length; i++){
            if (flags[i] == 1)
                dbAttributes.add(new DBAttribute<>(header[i], DBAttribute.LONG_CONTENT, true));
        }

        // prepare dbTuples
        List<DBTuple> dbTuples = new ArrayList<>();
        while (reader.readRecord()){
            String[] raw_tuple = reader.getValues();
            for(String str : raw_tuple){
                System.out.print(str + " ");
            }
            System.out.println();
            DBTuple dbt = new DBTuple(raw_tuple.length);
            dbt.add(new DBValue<>(raw_tuple[0]));
            for (int i=1; i<header.length; i++){
                if (flags[i] == 1)
                    dbt.add(new DBValue<>(Integer.parseInt(raw_tuple[i])));
            }
            System.out.println(dbt.getValue_list().size());
            dbTuples.add(dbt);
        }

        System.err.println(dbTuples.size());

        // prepare dbTable Name
        String dbTableName = "FileContributorMatrix" + repoID;

        DBTable dbTable = new DBTable(dbTableName, dbTuples, dbAttributes);

        dbTable.insertToSQL(connection, Update);
    }

}
