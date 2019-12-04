package CommitKeyWord;

import Contri.CommitMessages;
import Repository.GitRepository;
import Repository.GitRepositoryFactory;
import com.csvreader.CsvWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.nio.charset.Charset;
import java.util.*;

public class CommitAndPerson {
    private Git git;
    private List<CommitMessages> commitMsgs;
    private Map<String, CommitLifeMap> person;
    private List<String> personName;
    public int threhold = 100;//限制高于100的人数才能进入名单personName
    Date minDate;   //起始日期
    Date maxDate;   //终止日期
    public CommitAndPerson(Git input_git, int theh){
        git = input_git;
        threhold = theh;
        BuildCommitListAndPerson();
        //barCode = new BarCode(commitMsgs, false, null, null);
    }

    //补充完整BarCode功能实现
    public void BuildCommitListAndPerson (){
        List<RevCommit> commitList = new ArrayList<>();
        Iterable<RevCommit> commits = null;
        try {
            commits = git.log().call();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        for(RevCommit commit:commits){
            commitList.add(commit);
        }
        commitMsgs = new ArrayList<CommitMessages>();
        person = new HashMap<String, CommitLifeMap>();
        personName = new ArrayList<>();
        for(int i = 0; i < commitList.size() - 1; i++) {
            RevCommit commit = commitList.get(i);
            CommitMessages msg = new CommitMessages(commit);
            Date date = msg.getCommitTime();
            //FIXME:为了便于统计，强制把时分秒清零了...
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            commitMsgs.add(msg);
            if(minDate==null||maxDate==null){
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
            String id = msg.getAuthorName();
            if (person.containsKey(id)) {
                CommitLifeMap mp = person.get(id);
                mp.insert(msg.getCommitTime(), msg.GetCommit().getShortMessage());
                person.put(id, mp);
            } else {
                CommitLifeMap cmp = new CommitLifeMap();
                cmp.insert(msg.getCommitTime(), msg.GetCommit().getShortMessage());
                person.put(id, cmp);
            }

        }
        for(String entry : person.keySet()){
            personName.add(entry);
        }
        System.out.println("total commit size:" + commitList.size());
    }

    //  按天输出
    public void SaveByDay(String filePath) throws Exception{
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
        // 写表头
        String[] header = new String[personName.size() + 2];
        header[0] = "Date";
        header[1] = "All";
        for(int i = 2; i < personName.size() + 2; i++){
            header[i] = personName.get(i-2);
        }
        csvWriter.writeRecord(header);
        Map<Date, Integer> mp = new HashMap<>();
        for(int i = 0;i < commitMsgs.size(); i++){
            CommitMessages commit = commitMsgs.get(i);
            Date dt = commit.getCommitTime();
            if(mp.containsKey(dt)){
                Integer it = mp.get(dt) + 1;
                mp.put(dt, it);
            }
            else{
                mp.put(dt, new Integer(1));
            }
        }
        mp = sortMapByKey(mp);
        for(Map.Entry<Date, Integer> entry: mp.entrySet()){
            Date key = entry.getKey();
            String time = Integer.toString(key.getYear() + 1900) + "/" + Integer.toString(key.getMonth() + 1)+ "/" + Integer.toString(key.getDate());
            //System.out.println("date: "+time);
            csvWriter.write(time);
            //csvWriter.write(entry.getValue().toString());
            Date dt = entry.getKey();
            List<String> lines = new ArrayList<String>();
            int sum_add = 0;
            int sum_del = 0;
            int sum_mod = 0;
            int sum_fix = 0;
            for(String name:personName){
                CommitLifeMap oneperson = person.get(name);
                int add = oneperson.GetKindByDate(dt, 1);sum_add+=add;
                int del = oneperson.GetKindByDate(dt, 2);sum_del+=del;
                int mod = oneperson.GetKindByDate(dt, 3);sum_mod+=mod;
                int fix = oneperson.GetKindByDate(dt, 4);sum_fix+=fix;
                //csvWriter.write(String.valueOf(add) + "-" + String.valueOf(del) + "-" +String.valueOf(mod) + "-" +String.valueOf(fix));
                lines.add(String.valueOf(add) + "-" + String.valueOf(del) + "-" +String.valueOf(mod) + "-" +String.valueOf(fix));
            }
            csvWriter.write(String.valueOf(sum_add) + "-" + String.valueOf(sum_del) + "-" +String.valueOf(sum_mod) + "-" +String.valueOf(sum_fix));
            for(String line:lines){
                csvWriter.write(line);
            }
            csvWriter.endRecord();
        }
        csvWriter.close();
    }

    //Test
    /*public static void main(String args[]) {
        String GitAddress = "https://github.com/Microsoft/microsoft.github.io.git";
//        String GitAddress = "https://github.com/OpenSrcRepoDataDigging/DailyLog.git";
        try {
//            GitRepository repo = new GitRepositoryFactory().cloneRepositoryFrom(GitAddress); //git clone到临时目录
//            GitRepository repo = new GitRepositoryFactory().cloneRepositoryFromTo(GitAddress,"E:\\GitMiner_Codes\\Test"); //git clone到目标目录
            GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("D:\\课程学习\\创新项目\\MyCode\\alluxio\\alluxio"); //获得本地项目的引用
            Git git = repo.getGit();
            if (git == null){
                System.out.println("Git is null");
                return;
            }
            CommitAndPerson cp = new CommitAndPerson(git, 0);
            cp.SaveByDay("D:\\课程学习\\创新项目\\MyCode\\SaveByDay.csv");
            System.out.println("Ready to cancel");

            //TODO: 测试时为了速度，使用本地项目，但是记得如果用临时目录，要删除
            //repo.deleteRepository();
        }catch (Exception e){
            e.printStackTrace();
        }

    }*/

    //对时间进行排序
    private Map<Date, Integer> sortMapByKey(Map<Date, Integer> map){
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<Date, Integer> sortMap = new TreeMap<Date, Integer>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }
}
