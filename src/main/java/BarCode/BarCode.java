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
        FormatBarCode();
    }

    public void FormatBarCode(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = dateFormat.format(minDate);
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
                    System.out.println(ins);
                }else{
                    barcode.put(ins,new BarInfo());
                }
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }




    @Override
    public String toString() {
        return "BarCode{" +
                "barcode=" + "\n"
                + barcode +
                '}';
    }
}
