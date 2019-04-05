package Repository;

import BarCode.BarCode;
import BarCode.BarCodeDraw;
import Contri.ContributorMap;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommitMessageTypeTest {

    @Test
    public void testTpye(){
        GitRepository repo = new GitRepositoryFactory().openLocalRepositoryFrom("\\E:\\大三下\\创新项目\\Git-Repository-Miner-Codes"); //获得本地项目的引用
        ContributorMap contributorMap = repo.getContributorMap();
        contributorMap.getMaps().forEach((author,contributor)->{
            System.out.println("Author:"+author);
            contributor.getCommitList().forEach(msg->{
                System.out.println(msg.getCommitTime());
            });
            BarCode bar = new BarCode(contributor.getCommitList());
            System.out.println(bar);
            System.out.println("------------");
            BarCodeDraw barCodeDraw = new BarCodeDraw(bar);
            barCodeDraw.drawChartPanel();
        });
    }
}
