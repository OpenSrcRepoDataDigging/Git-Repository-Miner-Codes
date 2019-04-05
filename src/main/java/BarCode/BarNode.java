package BarCode;

import java.util.Date;
/*
 * @Author: Young
 * @Description: 存储每个条形码的信息
 * */
public class BarNode {
    Date date;
    BarInfo info;

    public BarNode(Date date, BarInfo info) {
        this.date = date;
        this.info = info;
    }

}
