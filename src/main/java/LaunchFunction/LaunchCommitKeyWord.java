package LaunchFunction;

import CommitKeyWord.CommitKeyWord;
import org.eclipse.jgit.api.Git;

public class LaunchCommitKeyWord {
    public static void Lauch(Git git) throws Exception{
        System.out.println("Ready to calculate Commit Key Word");
        CommitKeyWord commitKeyWord = new CommitKeyWord(git);
        commitKeyWord.CalculateKeyWord("D://KeyWords.csv");

        System.out.println("Ready to save");
        //SaveLOC2csv.Save2csv(calculateLOC.getContributorMap(), "D:/");
    }
}
