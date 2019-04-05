package CommitKeyWord;

import com.csvreader.CsvWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Map;
import java.util.Date;
import java.util.TreeMap;

class NumCounter{
    public int add = 0;//增加属性
    public int del = 0;//去掉depreciation
    public int mod = 0;//代码重构
    public int fix = 0;//Fix Bugs
    public void init(){
        add = 0;//增加属性
        del = 0;//去掉depreciation
        mod = 0;//代码重构
        fix = 0;//Fix Bugs
    }
    public NumCounter(String message){
        init();
        append(message);
    }
    public void append(String message){
        String[] sentence = message.split("\\s+");
        for(String word : sentence)
        {
            if(word.contains("add") || word.contains("Add") || word.contains("Creat")){
                add++;
            }
            else if(word.contains("Delet") || word.contains("delet")){
                del++;
            }
            else if(word.contains("Remove") || word.contains("Updat") || word.contains("Refactor")){
                mod++;
            }
            else if(word.contains("Fix") || word.contains("FIX") || word.contains("Chang"))
                fix++;
        }
    }
}
//排序因子
class MapKeyComparator implements Comparator<Date> {

    @Override
    public int compare(Date date1, Date date2) {

        return date1.compareTo(date2);
    }
}

public class CommitLifeMap {
    private Map<Date, NumCounter> map;

    public CommitLifeMap(){
        map = new TreeMap<Date, NumCounter>();
        map.clear();
    }
    //TODO:插入数值
    public void insert(Date date, String message){
        date.setSeconds(0);
        date.setMinutes(0);
        date.setHours(0);
        date.setDate(1);
        if(!map.containsKey(date))
            map.put(date,new NumCounter(message));
        else{
            NumCounter numCounter = map.get(date);
            numCounter.append(message);
            map.put(date, numCounter);
        }

    }
    //TODO:保存文件
    public void SaveAsFile(String filePath) throws Exception{
        sortMapByKey();
        if (map == null || map.isEmpty()){
            System.out.println("map is NULL!!");
            return ;
        }
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
        // 写表头
        String[] headers = {"Data","Add", "Del", "Mod", "Fix"};
        csvWriter.writeRecord(headers);
        for (Date key : map.keySet()) {
            String time = Integer.toString(key.getYear() + 1900) + "/" + Integer.toString(key.getMonth() + 1);
            //String time = Integer.toString(key.getYear() + 1900) + "." + String.format("%02d",key.getMonth() + 1);
            System.out.println(key + "  " + time + " ：" + "add:" + map.get(key).add + ",del="+map.get(key).del
                    + "mod:" + map.get(key).mod + ",fix:" + map.get(key).fix);
            csvWriter.write( time);
            csvWriter.write(Integer.toString(map.get(key).add));
            csvWriter.write(Integer.toString(map.get(key).del));
            csvWriter.write(Integer.toString(map.get(key).mod));
            csvWriter.write(Integer.toString(map.get(key).fix));
            csvWriter.endRecord();
        }
        csvWriter.close();
    }

    //对时间进行排序
    private Map<Date, NumCounter> sortMapByKey(){
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<Date, NumCounter> sortMap = new TreeMap<Date, NumCounter>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}
