package Repository;

import BarCode.BarCode;
import BarCode.BarCodeDraw;
import BarCode.BarCodeSave;
import Contri.CommitMessages;
import Contri.ContributorMap;
import FirstCommit.getFirstCommitTimesList;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommitMessageTypeTest {

    @Test
    public void testTpye(){
//        GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("E:\\大三上\\JAVA程序设计\\java-2018f-homework"); //获得本地项目的引用
//        GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("E:\\大三下\\创新项目\\Git-Repository-Miner-Codes"); //获得本地项目的引用
        GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("E:\\大三下\\创新项目\\alluxio"); //获得本地项目的引用

//        AllContribution(repo);
//        try {
//            LaunchLOC.Lauch(repo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        GetAllAndEachContribution(repo);
//        getFirstCommitLogs(repo);

    }

    void AllContribution(GitRepository repo){
        List<CommitMessages> commitList = new ArrayList<>();    //记录该开发者的Commit信息
        Iterable<RevCommit> commits = null;
        try {
            commits = repo.getGit().log().call();
            for(RevCommit commit:commits){
                commitList.add(new CommitMessages(commit));
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        BarCode bar = new BarCode(commitList,false,null,null);
        bar.getCommitListByWeek();
//        System.out.println(bar);
        System.out.println("------------");
//        BarCodeDraw barCodeDraw = new BarCodeDraw(bar);
//        barCodeDraw.drawChartPanel();

//        ArrayList<BarCode> barCodeArrayList = new ArrayList<BarCode>();
//        barCodeArrayList.add(bar);
//        barCodeArrayList.add(bar);
//        BarCodeSave saveBar = new BarCodeSave(barCodeArrayList);
//        saveBar.SaveBarCode("allBarCode.csv");
    }

    void EachContribution(GitRepository repo){
        ContributorMap contributorMap = repo.getContributorMap();
        contributorMap.getMaps().forEach((author,contributor)->{
            System.out.println("Author:"+author);
            contributor.getCommitList().forEach(msg->{
                System.out.println(msg.getCommitTime());
            });
            BarCode bar = new BarCode(contributor.getCommitList(),false,null,null);
            System.out.println(bar);
            System.out.println("------------");
            BarCodeDraw barCodeDraw = new BarCodeDraw(bar);
            barCodeDraw.drawChartPanel();
        });
    }

    void GetAllAndEachContribution(GitRepository repo){
        ArrayList<BarCode> barCodeArrayList = new ArrayList<BarCode>(); //记录项目和各个贡献者的条形码。
        List<CommitMessages> commitList = new ArrayList<>();    //记录项目总体的Commit信息
        Iterable<RevCommit> commits = null;
        try {
            commits = repo.getGit().log().call();
            for(RevCommit commit:commits){
                commitList.add(new CommitMessages(commit));
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        BarCode bar = new BarCode(commitList,false,null,null);
        System.out.println("------------");
        barCodeArrayList.add(bar);
        ContributorMap contributorMap = repo.getContributorMap();
        contributorMap.getMaps().forEach((author,contributor)->{
//            System.out.println("Author:"+author);
//            contributor.getCommitList().forEach(msg->{
//                System.out.println(msg.getCommitTime());
//            });
            BarCode barTmp = new BarCode(contributor.getCommitList(),true,bar.getMinDate(),bar.getMaxDate());
//            System.out.println(barTmp);
            barCodeArrayList.add(barTmp);
//            System.out.println("------------");
        });

        BarCodeSave saveBar = new BarCodeSave(barCodeArrayList);
        saveBar.SaveBarCode(repo.getGit().getRepository().getDirectory().getParentFile().getName()+".csv");
    }

    void getFirstCommitLogs(GitRepository repo){
        getFirstCommitTimesList fsList = new getFirstCommitTimesList(repo);
        fsList.getCtList();
        fsList.saveFirstTimeList();

    }

}
