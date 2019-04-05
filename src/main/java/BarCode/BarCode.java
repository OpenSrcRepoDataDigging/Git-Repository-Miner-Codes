package BarCode;

import Contri.CommitMessages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * @Author: Young
 * @Description: 根据一个CommitList创建一个条形码
 * */
public class BarCode {
    List<CommitMessages> commitMsgs;
    HashMap<Date,BarInfo> barcode;
    List<BarNode> barNodeList;
    Date minDate;
    Date maxDate;

    public BarCode(List<CommitMessages> commitMsgs) {
        this.commitMsgs = commitMsgs;
        barcode = new HashMap<Date, BarInfo>();
        commitMsgs.forEach(msg->{
            Date date = msg.getCommitTime();
            //FIXME:为了便于统计，强制把时分秒清零了...
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            if(barcode.containsKey(date)){
                barcode.get(date).increseTimes();
            }else{
                barcode.put(date,new BarInfo());
                barcode.get(date).increseTimes();
            }
            //计算最早和最晚的时间
            if(minDate==null){
                minDate = date;
                maxDate = date;
            }else{
                if(date.after(maxDate)){
                    maxDate=date;
                }
                if(date.before(minDate)){
                    minDate=date;
                }
            }
        });
        System.out.println("minDate:"+minDate);
        System.out.println("maxDate:"+maxDate);
        FormatBarCode();
    }


    //对日期格式化
    public void FormatBarCode(){
        barNodeList = new ArrayList<BarNode>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = dateFormat.format(minDate);
        //FIXME:Java有个2017的提交太毒瘤，让条形码很恶心所以这里测试用改一下
        startTime = "2018-09-09";
        //TODO:Java有个2017的提交太毒瘤，让条形码很恶心所以这里测试用改一下
        String endTime = dateFormat.format(maxDate);
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)\
            System.out.println("------------Formatting------------");
            while (tempStart.before(tempEnd)) {
                Date ins = tempStart.getTime();
                if(barcode.containsKey(ins)){
                    barNodeList.add(new BarNode(ins,barcode.get(ins)));
//                    System.out.println(ins);
                }else{
                    barNodeList.add(new BarNode(ins,new BarInfo()));
//                    barcode.put(ins,new BarInfo());
                }
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        sortBarNodeList();
        barNodeList.sort(new Comparator<BarNode>()
        {
            public int compare(BarNode o1, BarNode o2)
            {
                if(o2.date!=null&&o1.date!=null&&
                        o2.date.before(o1.date)){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
//        System.out.println("After Sort sortBarNodeList:");
//        for(int i=0;i<barNodeList.size()-1;i++){
//            if(barNodeList.get(i).date.before(barNodeList.get(i+1).date)){
//                System.out.print(1);
//            }else{
//                System.out.print(-1);
//            }
//            System.out.println(barNodeList.get(i).date+""+barNodeList.get(i).info);
//        }
    }

    //FIXME:作废
    public void sortBarNodeList(){
        Collections.sort(barNodeList, new Comparator<BarNode>()
        {
            public int compare(BarNode o1, BarNode o2)
            {
                if(o2.date!=null&&o1.date!=null&&
                        o2.date.before(o1.date)){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    }

    //FIXME:作废
    public void sortHashMap(){
        List<Map.Entry<Date,BarInfo>> list_Data = new ArrayList<Map.Entry<Date,BarInfo>>(barcode.entrySet());
        System.out.println("Before Sort "+list_Data);
        Collections.sort(list_Data, new Comparator<Map.Entry<Date,BarInfo>>()
        {
            public int compare(Map.Entry<Date,BarInfo> o1, Map.Entry<Date,BarInfo> o2)
            {
                if(o2.getKey()!=null&&o1.getKey()!=null&&o2.getKey().after(o2.getKey())){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        System.out.println("After Sort "+list_Data);
    }

    public List<BarNode> getBarNodeList() {
        return barNodeList;
    }

    @Override
    public String toString() {
        return "BarCode{" +
                "barcode=" + "\n"
                + barcode +
                '}';
    }
}
