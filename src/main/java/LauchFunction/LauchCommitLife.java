package LauchFunction;

import CommitKeyWord.CommitLife;
import org.eclipse.jgit.api.Git;

public class LauchCommitLife {
    public static void Lauch(Git git) throws Exception{
        System.out.println("Ready to Commit Life");
        CommitLife  commitLife= new CommitLife(git);
        commitLife.ShowCommit("D://CommitLife.csv");

        System.out.println("Ready to save");
    }
}
