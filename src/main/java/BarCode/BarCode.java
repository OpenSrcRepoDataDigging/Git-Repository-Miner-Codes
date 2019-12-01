package BarCode;

import Contri.CommitMessages;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

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
    List<BarNode> barNodeWeekList;  //按照星期为粒度的List
    Date minDate;   //条形码起始日期
    Date maxDate;   //条形码终止日期
    boolean setScale; //是否设定起始和终止日期
    BarInfo allMsgReg; //记录总和信息

    public BarCode(List<CommitMessages> commitMsgs,boolean setScale,Date minD,Date maxD) {
        this.commitMsgs = commitMsgs;
        this.setScale = setScale;
        if(setScale){
            minDate = minD;
            maxDate = maxD;
        }
        barcode = new HashMap<Date, BarInfo>();
        allMsgReg = new BarInfo();
        commitMsgs.forEach(msg->{
//            msg.displayMessages();
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
            allMsgReg.increseTimes();
            //计算最早和最晚的时间
            if(!setScale){
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
            }
        });
//        System.out.println("minDate:"+minDate);
//        System.out.println("maxDate:"+maxDate);
        FormatBarCode();
        //FIXME: 是否要以一周为单位，2019/4/12 1：17 这里决定以天为单位然后算一晚上矩阵
//        barNodeList = getCommitListByWeek();
//        System.out.println(barNodeList);
    }

    //从已有的List中生成新的list
    public List<BarNode> getCommitListByWeek(){
        if(barNodeWeekList != null){
            return barNodeWeekList;
        }
        barNodeWeekList = new ArrayList<BarNode>();
        Map<String,Integer> temp = getWeekAndYear(barNodeList.get(0).date);
        BarNode node = new BarNode(barNodeList.get(0).date,new BarInfo(barNodeList.get(0).info.getCommitTimes()));
        for(int i=1;i<barNodeList.size();i++){
            if(getWeekAndYear(node.date).equals(getWeekAndYear(barNodeList.get(i).date))){
                node.info.addTimes(barNodeList.get(i).info.getCommitTimes());
                continue;
            }else{
//                System.out.println(node);
                barNodeWeekList.add(node);
                node = new BarNode(barNodeList.get(i).date,new BarInfo(barNodeList.get(i).info.getCommitTimes()));
            }
        }
        barNodeWeekList.add(node);
//        System.out.println(node);
//        barNodeList.forEach(node->{
//            System.out.println(getWeekAndYear(node.date));
//        });
//        System.out.println("size="+barNodeList.size()+",week size="+barNodeWeekList.size());
        return barNodeWeekList;
    }

    public static Map<String,Integer> getWeekAndYear(Date date) {
        Map<String,Integer> result =  new HashMap<String,Integer>();
        Calendar cal = Calendar.getInstance();

        //设置一周的开始,默认是周日,这里设置成星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatMon = new SimpleDateFormat("MM");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        Date d = date;

        cal.setTime(d);
        int month = Integer.valueOf(formatMon.format(d));
        int year = Integer.valueOf(formatYear.format(d));

        int week = cal.get(Calendar.WEEK_OF_YEAR);
        result.put("week", week);
        if(week == 1 && month == 12){
            result.put("year", year + 1);
        }else{
            result.put("year", year);
        }

        return result;
    }

    //对日期格式化
    public void FormatBarCode(){
        barNodeList = new ArrayList<BarNode>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = dateFormat.format(minDate);
        //FIXME:Java有个2017的提交太毒瘤，让条形码很恶心所以这里测试用改一下
//        startTime = "2018-09-09";
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
//            System.out.println("------------Formatting------------");
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

    public Date getMinDate() {
        return minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public BarInfo getAllMsgReg() {
        return allMsgReg;
    }

    @Override
    public String toString() {
        return "BarCode{" +
                "barcode=" + "\n"
                + barcode +
                '}';
    }


}
