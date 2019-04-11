package FirstCommit;

import Contri.ContributorMap;
import Repository.GitRepository;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;

public class getFirstCommitTimesList {
    GitRepository repo;
    ArrayList<CommitTimesNode> ctList;

    public getFirstCommitTimesList(GitRepository repo) {
        this.repo = repo;
    }

    public void getCtList(){
        ContributorMap contributorMap = repo.getContributorMap();
        ArrayList<FirstCommitNode> first = new ArrayList<FirstCommitNode>();
        contributorMap.getMaps().forEach((author,contributor)->{
            first.add(new FirstCommitNode(author,contributor.getFirstCommitTime()));
//            System.out.println("Author:"+author+",First Commit time:"+(contributor.getFirstCommitTime().getYear()+1900)+"-"+(contributor.getFirstCommitTime().getMonth()+1));
        });
        //对list按照排序
        first.sort(new Comparator<FirstCommitNode>()
        {
            public int compare(FirstCommitNode o1, FirstCommitNode o2)
            {
                if(o2.getDate()!=null&&o1.getDate()!=null&&
                        o2.getDate().before(o1.getDate())){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        ctList = new ArrayList<>();
        first.forEach(c->{
            System.out.println("Author:"+c.getAuthor()+",First Commit time:"+(c.getDate().getYear()+1900)+"-"+(c.getDate().getMonth()+1));
            boolean flag = false;
            for(CommitTimesNode a :ctList){
                if(a.dateEqual(c.getDate())){
                    a.increaseTimes();
                    flag = true;
                    break;
                }
            }
            if(flag == false){
                ctList.add(new CommitTimesNode(c.getDate()));
            }
        });
        ctList.forEach(c->{
            System.out.println(c.getYear_m()+c.getCounts());
        });
    }

    public void saveFirstTimeList(){
        CsvWriter csvWriter = new CsvWriter("首次提交次数和提交时间.csv", ',', Charset.forName("GBK"));
        //CsvWriter csvWriter = new CsvWriter(filePath);
        // 写表头
        String[] header = new String[2];
        header[0] = "Date";
        header[1] = "Times";

        try {
            csvWriter.writeRecord(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //遍历记录
        for(int i=0;i<ctList.size();i++){
            try {
                csvWriter.write(ctList.get(i).year_m);
                csvWriter.write(String.valueOf(ctList.get(i).getCounts()));
                csvWriter.endRecord();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        csvWriter.close();
        System.out.println("Finish Save BarCode");
    }
}
