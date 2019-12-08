package LaunchFunction.DataConvert;

import LaunchFunction.DataConvert.model.*;
import com.csvreader.CsvReader;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
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
/*
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
            new ContributorNetworkConverter().convert("/home/miragelyu/.gitminer/5/csv/fcm.csv_original", connection, "0", false, Arrays.asList(filter));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

 */

    @Override
    public void convert(String csvfilepath, Connection connection, String repoID, boolean Update, List<String> filter) throws Exception {
        CsvReader reader = new CsvReader(csvfilepath, ',', Charset.forName("UTF-8"));
        reader.readHeaders();
        String[] header = reader.getHeaders();

        List<DBAttribute> dbAttributes = new ArrayList<>();
        List<DBTuple> dbTuples = new ArrayList<>();

        List<String[]> raw_tuples = new ArrayList<>();
        while(reader.readRecord()){
            String[] raw_tuple = reader.getValues();
            raw_tuples.add(raw_tuple);
        }

        int[] flags = new int[raw_tuples.size()];
        for (int i=0; i<flags.length; i++){
            if (filter.contains(raw_tuples.get(i)[0])){
                flags[i] = 1;
            }
            else{
                flags[i] = 0;
            }
        }

        for (int i:flags){
            System.out.print(i + " ");
        }

        for (int i=0; i<raw_tuples.size(); i++){
            for (int j=i+1; j<raw_tuples.size(); j++){
                if (raw_tuples.get(i)[0].toUpperCase().equals(raw_tuples.get(j)[0].toUpperCase())){
                    String[] tmp = raw_tuples.get(j);
                    tmp[0] += "_dt";
                    raw_tuples.set(j, tmp);
                }
            }
        }

        dbAttributes.add(new DBAttribute<>("name", DBAttribute.STRING_CONTENT, true));

        for (int i=0; i<raw_tuples.size(); i++){

            String[] t1 = raw_tuples.get(i);

            if(flags[i] == 1) {
                dbAttributes.add(new DBAttribute<>(t1[0], DBAttribute.LONG_CONTENT, true));
                DBTuple dbt = new DBTuple(filter.size() + 1);
                dbt.add(new DBValue<>(t1[0]));
                for (int j=0; j<raw_tuples.size(); j++) {
                    if (flags[j] == 1) {
                        String[] t2 = raw_tuples.get(j);
                        int result = getCommonFileNumber(t1, t2);
                        dbt.add(new DBValue<>(result));
                    }
                }
                dbTuples.add(dbt);
            }
        }

        for (DBTuple dbt : dbTuples){
            System.out.println(dbt.getValue_list().get(0).getDbvalue());
        }

        String dbTableName = "ContributorNetworkMatrix" + repoID;
        DBTable dbTable = new DBTable(dbTableName, dbTuples, dbAttributes);

        dbTable.insertToSQL(connection, Update);
    }
}
