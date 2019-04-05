package BarCode;

/*
 * @Author: Young
 * @Description: 条形码的纵坐标（要以什么为准则来显示条形码）
 * */

public class BarInfo {
    private int commitTimes; //提交的次数
    private int LOC; //FIXME:这里根据条形码的初始定义，先按照提交次数来确定

    public BarInfo() {
        this.commitTimes = 1;
    }

    public void addTimes(int add) {
        this.commitTimes += add;
    }

    public void increseTimes(){
        this.commitTimes++;
    }

    public int getCommitTimes() {
        return commitTimes;
    }

    @Override
    public String toString() {
        return "BarInfo{" +
                "commitTimes=" + commitTimes +
                '}'+"\n";
    }
}
