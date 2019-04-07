package CommitKeyWord;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;

public class CommitLife {
    private Git git;
    private CommitLifeMap commitLifeMap;
    public CommitLife(Git input_git){
        git = input_git;
        commitLifeMap = new CommitLifeMap();
    }

    public void ShowCommit(String filePath) throws Exception {
        List<RevCommit> commitList = new ArrayList<>();
        Iterable<RevCommit> commits = git.log().call();
        for(RevCommit commit:commits){
            commitList.add(commit);
        }
        for(int i = 0; i < commitList.size() - 1; i++)
        {
            RevCommit commit = commitList.get(i);
            System.out.println("COMMIT Time: " + commit.getAuthorIdent().getWhen());
            commitLifeMap.insert(commit.getAuthorIdent().getWhen(), commit.getShortMessage());
            //System.out.println("\033[31;1m" + "=======================This is the " + i + "th Commit======================="+"\033[0m");
            //System.out.println("");
            //ShowLOC(commitList.get(i), commitList.get(i + 1));
        }
        System.out.println("total size:" + commitList.size());

        System.out.println("Begin to Save");
        commitLifeMap.SaveAsFile(filePath);

    }
}
