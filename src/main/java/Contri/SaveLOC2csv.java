package Contri;

import com.csvreader.CsvWriter;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SaveLOC2csv {
    public SaveLOC2csv(){

    }
    /**
     * 传入文件目录和contributorMap，保存其中的用户名和LOC
     * @param contributorMap
     * @param filePath 文件路径
     * @return null
     * @throws Exception
     */
    public static void Save2csv(ContributorMap contributorMap, String filePath) throws Exception{
        //获得map
        HashMap<String, Contributor> map = contributorMap.getMaps();
        //存用户数据
        SaveLOC2csv.SaveAuthor(map, filePath + "/Author.csv");
        //存对应的LOC数据
        SaveLOC2csv.SaveLOC(map,filePath + "/LOC.csv");

    }

    /**
     * 传入map，保存其中的用户名
     * @param map
     * @param filePath 文件路径
     * @return null
     * @throws Exception
     */
    public static void SaveAuthor(HashMap<String, Contributor> map, String filePath) throws Exception{
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
        //CsvWriter csvWriter = new CsvWriter(filePath);
        // 写表头
        String[] headers = {"AuthorName"};
        csvWriter.writeRecord(headers);
        //遍历用户名
        for (Map.Entry<String, Contributor> entry : map.entrySet()) {
            csvWriter.write(entry.getKey());
            csvWriter.endRecord();
        }

        csvWriter.close();
        System.out.println("Finish Save AuthorName");

    }

    /**
     * 传入map，保存其中的LOC
     * @param map
     * @param filePath 文件路径
     * @return null
     * @throws Exception
     */
    public static void SaveLOC(HashMap<String, Contributor> map, String filePath) throws Exception{
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
        //CsvWriter csvWriter = new CsvWriter(filePath);
        // 写表头
        String[] headers = {"AuthorName","LOC_Add","LOC_Del"};
        csvWriter.writeRecord(headers);
        //遍历记录
        for (Map.Entry<String, Contributor> entry : map.entrySet()) {
            csvWriter.write(entry.getKey());
            Contributor contributor = entry.getValue();
            csvWriter.write(String.valueOf(contributor.getLOC_Add_All()));
            csvWriter.write(String.valueOf(contributor.getLOC_Delete_All()));
            csvWriter.endRecord();
        }

        csvWriter.close();
        System.out.println("Finish Save LOC");

    }
}
