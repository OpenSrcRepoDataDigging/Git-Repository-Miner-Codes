package BarCode;
import Contri.Contributor;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/*
 * @Author: Young
 * @Description: 将条形码信息存储到CSV文件中
 * */

public class BarCodeSave {
    ArrayList<BarCode> barCodeList;

    public BarCodeSave(ArrayList<BarCode> barCodeList) {
        this.barCodeList = barCodeList;
        barCodeList.sort(new Comparator<BarCode>()
        {
            public int compare(BarCode o1, BarCode o2)
            {
                if(o2.getAllMsgReg()!=null&&o1.getAllMsgReg()!=null&&
                        o2.getAllMsgReg().getCommitTimes()>o1.getAllMsgReg().getCommitTimes()){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    }

    /**
     * @param filename :文件名
     * @return null
     * @throws Exception
     */
    public void SaveBarCode(String filename){
        CsvWriter csvWriter = new CsvWriter(filename, ',', Charset.forName("GBK"));
        //CsvWriter csvWriter = new CsvWriter(filePath);
        // 写表头
        int size = barCodeList.size()+1;
        String[] header = new String[size];
        header[0] = "Date";
        header[1] = "All";
        if(size>2){
            for(int i=1;i<size-1;i++){
                header[i+1] = barCodeList.get(i).commitMsgs.get(0).getAuthorName();
            }
        }

        try {
            csvWriter.writeRecord(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //遍历记录
        int dateLength = barCodeList.get(0).getBarNodeList().size(); //一共有多少天的记录
        for(int i=0;i<dateLength;i++){
            try {
                int authorSize = barCodeList.size(); //一共有多少个人（默认第一个为全部）
                csvWriter.write(barCodeList.get(0).getBarNodeList().get(i).date.toString());
                for(int j=0;j<barCodeList.size();j++){
                    csvWriter.write(String.valueOf(barCodeList.get(j).getBarNodeList().get(i).info.getCommitTimes()));
                }
                csvWriter.endRecord();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
        System.out.println("Finish Save BarCode");

    }

}
