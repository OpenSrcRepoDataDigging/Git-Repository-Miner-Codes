package DataConvert;

import LaunchFunction.DataConvert.FileContributorMatrixConverter;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;

public class FileContributorMatrixConverterTest {

    @Test
    public void ConverterTest(){
        try {
            Class.forName("org.sqlite.JDBC");
            String pathname = "/home/"+System.getProperty("user.name")+"/test.db";
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathname);

            String csvpath = "testcsvfile/sample.csv";

            new FileContributorMatrixConverter().convert(csvpath, connection);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
