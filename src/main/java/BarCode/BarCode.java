package BarCode;

import Contri.CommitMessages;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*
 * @Author: Young
 * @Description: 根据一个CommitList创建一个条形码
 * */
public class BarCode {
    List<CommitMessages> commitMsgs;
    HashMap<Date,BarInfo> barcode;

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
            }
        });
    }

    @Override
    public String toString() {
        return "BarCode{" +
                "barcode=" + "\n"
                + barcode +
                '}';
    }
}
