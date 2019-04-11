package FirstCommit;

import java.util.Date;

public class CommitTimesNode {
    Date date;
    String year_m;
    int counts;

    public CommitTimesNode(Date date) {
        this.date = date;
        this.year_m = String.valueOf(date.getYear()+1900)+"-"+String.valueOf(date.getMonth()+1);
        this.counts = 1;
    }

    public boolean dateEqual(Date td){
        return (td.getYear()==date.getYear())&&(td.getMonth()==date.getMonth());
    }

    public void increaseTimes(){
        this.counts++;
    }

    public String getYear_m() {
        return year_m;
    }

    public int getCounts() {
        return counts;
    }
}
