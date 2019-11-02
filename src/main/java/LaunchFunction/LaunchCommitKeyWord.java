package LaunchFunction;

import CommitKeyWord.CommitKeyWord;
import Repository.GitRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LaunchCommitKeyWord{
    private static final Logger LOG = Logger.getLogger(LaunchCommitKeyWord.class);
    static {
        BasicConfigurator.configure();
    }

    public static void Launch(GitRepository repo, String csvpath) throws Exception{
        LOG.debug("Ready to calculate Commit Key Word");
        CommitKeyWord commitKeyWord = new CommitKeyWord(repo.getGit());
        commitKeyWord.CalculateKeyWord(csvpath + "ckw.csv");

        LOG.debug("Ready to save");
    }
}
