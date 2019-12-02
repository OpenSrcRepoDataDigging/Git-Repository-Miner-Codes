package Contri;

import LOC.CalculateLOC;
import Repository.GitRepository;
import com.csvreader.CsvWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.nio.charset.Charset;
import java.util.*;

public class LOCAndLastestCommit {
    private CalculateLOC calculateLOC;
    private Map<String, Date> person;
    public LOCAndLastestCommit(GitRepository repo){
        CalculateLOC calculateLOC = new CalculateLOC(repo);
        try{
            calculateLOC.WorkOutLOC();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<RevCommit> commitList = new ArrayList<>();
        Iterable<RevCommit> commits = null;
        try {
            commits = repo.getGit().log().call();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        for(RevCommit commit:commits){
            commitList.add(commit);
        }
        person = new HashMap<String, Date>();
        for(int i = 0; i < commitList.size() - 1; i++) {
            RevCommit commit = commitList.get(i);
            CommitMessages msg = new CommitMessages(commit);
            Date date = msg.getCommitTime();
            //FIXME:为了便于统计，强制把时分秒清零了...
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            String id = msg.getAuthorName();
            if (person.containsKey(id)) {
                Date old = person.get(id);
                if(old.before(date))
                    person.put(id, date);
            } else {
                person.put(id, date);
            }

        }
    }
    public void SavePersonLOCAndLastestCommit(String filePath)throws Exception{
        ContributorMap contributorMap = calculateLOC.getContributorMap();
        CsvWriter csvWriter = new CsvWriter(filePath, ',', Charset.forName("UTF-8"));
        //CsvWriter csvWriter = new CsvWriter(filePath);
        // 写表头
        String[] headers = {"AuthorName","LOC_Add","LOC_Del", "LastestCommit"};
        csvWriter.writeRecord(headers);
        //遍历记录
        for (Map.Entry<String, Contributor> entry : contributorMap.getMaps().entrySet()) {
            csvWriter.write(entry.getKey());
            Contributor contributor = entry.getValue();
            csvWriter.write(String.valueOf(contributor.getLOC_Add_All()));
            csvWriter.write(String.valueOf(contributor.getLOC_Delete_All()));
            Date key = person.get(entry.getKey());
            String time = Integer.toString(key.getYear() + 1900) + "/" + Integer.toString(key.getMonth() + 1)+ "/" + Integer.toString(key.getDate());
            csvWriter.write(time);
            csvWriter.endRecord();
        }

        csvWriter.close();
        System.out.println("Finish Save PersonLOC And Lastest Commit");
    }
}
