package LaunchFunction;

import CommitKeyWord.CommitLife;
import Repository.GitRepository;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LaunchCommitLife {
    private static final Logger LOG = Logger.getLogger(LaunchCommitLife.class);
    static {
        BasicConfigurator.configure();
    }

    public static void Launch(GitRepository repo, String csvpath) throws Exception{
        LOG.debug("Ready to Commit Life");
        CommitLife  commitLife= new CommitLife(repo.getGit());
        commitLife.ShowCommit(csvpath + "cl.csv");

        LOG.debug("Ready to save");
    }
}
